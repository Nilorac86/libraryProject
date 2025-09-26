### 5. Säkerhetsarkitektur och dokumentation
- Förklara JWT vs sessions: fördelar, nackdelar och när man använder vad

I JWT skickas informationen (det som är inställt i playloaden) som ett JSON objekt om den autentiserade användaren  
direkt i token som sedan skickas med vid varje request. Då behöver inte datan kontrolleras i databasen och det   
minskar belastningen på servern. Eftersom JWT är statless behöves ingen sessioninformation vilket ökar skalbarhet.  
Nackdelar med JWT kan vara att den fungerar så länge den är giltig t.ex 1h. Det går att lösa genom att man använder  
en blacklist vid utloggning och då fungerar inte den token längre. Användaren behöver autentisera sig igen för att  
få en ny. Viktigt att se till att token inte kan lagras i frontend på något sätt eftersom den då blir oskyddad och   
angriparen kan få tillgång till hela eller delar av systemet beroende på vilken roll som ligger i den stulna token.  
Eftersom JWT tokens skickas som en cookie är risken liten för csrf. 
JWT är bäst att använda när man behöver stateless autentisering för moderna, distribuerade system skalbara applikationer  
  
Vid anvädning av sessions loggar en användare in, om användaren är autentiserad hämtar servern användarens information  
från databasen. Om användaren är autentiserad skickas ett unikt sessionID som sedan sparas i en cookie i användarens  
webbläsare som sedan följer med vid varje request så länge användaren är inloggad. 
Fördelar med sessions är att spring security skyddar mot sessionkapning genom att skapa nytt sessionID efter lyckad  
inloggning. Nackdelar är att det finns risk för session fixation. Eftersom datan lagras i servern finns det risk för 
hög minnes användning. Det är anpassat för webbappar men inte för mobilappar.

  
- Dokumentera er threat model och vilka hot ni skyddar mot
  

CSRF 
Hot- En angripare får en användare att omedvetet att skicka en förfrågan till användarens server.  
Skydd- Genom att använda JWT tokens och skicka in det i authorization header.  
Det är en liten risk att en angripare kan komma åt token.

XSS 
Hot- En användare skickar skadligt JavaScript via formulär eller API.  
Skydd- Input validering, HTML sanitizer, CSP
Skyddar mot att script sparas i databasen samt att det skickas till frontend.  
CSP skyddar mot att skript laddas från andra domäner.  
  
Session hijacking  
Hot- En angripare stjäl en sessions cookie och utger sig för att vara användaren som äger sessions cookie  
Skydd- Statless JWT inga sessions sparas i cookies  
  
Obehörig åtkomst  
Hot- Användare kommer åt data de inte borde ha tillgång till.
Skydd- Rollbaserad åtkomstkontroll i securityConfig.  
Preauthorized annotationer i både controll och service lagret.  
  
Brute force  
Hot- Angriparen gör upprepade försök att logga in med ett användarnamn.  
Skydd- Implementation av max antal inloggnings försök på en minut samt att konto låses efter misslyckade försök  
på en dag.  
Rate limit på alla endpoint.  
  
Weak Password
Hot- Använderen använder lösenord som är lätta att gissa. Angriparen kan då använda dessa för att komma in.  
Skydd- Validering av lösenord.  
  
SQL injection  
Hot- Angripare kan manipuletra databas frågor genom att skicka skadlig kod via formulär eller URL.  
Skydd- Validering av indata genom DTO och Valid annotationer.  
HTML sanitizer, använda JPA till att skapa databasfrågor i största möjliga utsträckning, att databasfrågor är  
skrivna på rätt sätt utan string konkatenering exempelvis.  

Man in the middle  
Hot- Data manipuleras eller avlyssnas genom överföring.  
Skydd- Konfigurerat i securityConfig.  
  
Clickjacking  
Hot- Angripare gömmer en dold ifram i appen och lurar användaren att klicka på något de inte ser.  
Skydd- X-frame- options är satt till deny i securityconfig.  
  
Informations läckage  
Hot- En angripare får tag på information om användaren via URL och försöker manipulera den.  
Skydd- Generera generiska meddelande till användaren där ingen information om användares uppgifter eller liknande.  
URL att inte. Att databas frågor skrivs på ett korrekt sätt.  

  

- Beskriv säkerhetsarkitekturen och designbeslut  

Jag har valt att bygga min app med flera lager av säkerhet för att skydda användare och   datan. Säkerheten är  
implementerad i fler nivåer från authentisiering och åtkonstkontroll   till skydd mot olika attacker så som   
brute force, XSS och clickjacking.  
  

Jag har implementerat

JWT token autentisering:  
Jag har valt autentisering genom  JWT tokens eftersom det är mer flexibelt än sessions. Om användaren är   
autentiserad genereras en access token som skickas med och   används via Authorization header som även skyddar    
mot csrf eftersom det inte skickas   via webbläsaren som cookies gör. Token skickas med vid varje request och  
innehåller   den autentiserade användarens nödvändiga data som ger åtkomst till rätt data. Jag   använder även   
en refresh token som har en längre giltighetstid. Den sparas som en http-   only cookie vilket gör att den inte   
kan nås via JavaScript och minska risken för XSS   attacker. Funktionen logout fungerar på det sättet att både   
access token och refresh   token blir ogiltig genom att de placeras i en blacklist och kan då inte användas mer.

Rollbaserad åtkomst  
I min applikation är rollbaserad åtkomst en stor del eftersom det styr vem eller vilka som har tillgång till datan.  
När en vanlig användare har loggat in får användaren åtkomst till   att se, göra, förlänga och returnera sina egna   
lån och kan inte komma åt andras lån.   Användaren kan inte komma åt endpoint som är låsta för admin, då får   
användaren 403   forbidden svar. Admin har däremot tillgång till alla endpoints i applikationen. Rollen   skickas  
med i varje request eftersom den finns i JWT token. Detta skyddas med   @PreAuthorized både i service och i   
controller metoderna, requestMatchers är   specifiserat i securityconfigfilterchain. Samt objektbaserad   
åtkomstkontroll.

RateLimit  
För att skydda systemet från brute force attacker och överbelastning har jag implementerat rateLimit.   
Jag använder OncePerRequestFilter som automatiskt kontrollerar alla inkommande requests. Om en användare gör   
för många requests till en och samma endpoint på kort tid får användaren statuskod 429 too many reqests och  
användaren blir temporärt blockerad att göra fler requests till den endpointen.

Inloggnings begränsning  
För att ytterligare skydda mot brute force attacker låses ett konto temporärt om en användare gör fem misslyckade  
inloggnings försök på en minut. Om antalet misslyckade försök överstiger en viss gräns på en dag spärras kontot   
helt. Det gör att det blir betydligt svårare för en användare att gissa lösenordet.

Input validering  
Input valideras genom att jag använder validerings-annoteringar på mina DTOer. Detta säkerställer att endast   
korrekt information sparas i databasen. För att undvika att känslig information läcker ut returnerar APIet DTOer.

HTML Sanitizer  
Jag använder en HTML sanitizer för att to bort potentiell skadlig HTML kod från användar inmatning.  
Detta skyddar databasen och användaren från XSS attacker.

Lösenords validering  
För att ett lösenord ska vara stark valideras detta genom att det måste innehålla en storbokstav,   
en liten bokstav, en siffra och vara minst åtta tecken långt. Detta gör lösenordet svårare att gissa.

Cors config  
CORS är konfigurerat för att endast tillåta anrop från specifika domäner, med vissa metoder och headers.  
Detta förhindrar obehöriga webbplatser från att anropa API:et från webbläsaren.

HTTP Strict Transport Security  
HSTS är aktiverat för att tvinga alla requests till att använda HTTPS. Detta skyddar mot   
man-in-the-middle-attacker där trafik via HTTP annars hade kunnat avlyssnas eller manipuleras.

Frame Options  
Frame options är satt till DENY för att förhindra att applikationen laddas in i en iframe.
Detta skyddar mot clickjacking-attacker där en angripare försöker lura användaren att klicka på något dolt   
genom att visa sidan ovanpå en annan.

Content Security Policy (CSP)  
CSP är konfigurerad så att endast resurser från den egna domänen får laddas, till exempel skript,   
bilder och stilmallar. Detta skyddar mot XSS-attacker genom att blockera skadliga resurser från externa källor.

Exception hantering  
Systemet returnerar generiska felmeddelande. Detta för att skydda mot att en angripare kommer åt  
känslig information.



- Analysera kvarvarande säkerhetsrisker och begränsningar

Största risken jag ser med nuvarande implementering är att det finns brister i lösenords validering.  
Finns flera vaideringar som kan läggas till för att göra det mycket säkrare mot brute force attacker.  
Att lösenordet måste innehålla minst ett specialtecken, inga uppreppande mönster, vanligt förekommande lösenord.   

Två faktors autentisering skulle göra skyddet ännu starkare och minska risken avsevärt mot angripare. 

Om en användare inte loggar ut sig från en offentlig dator kan token användas tills de går ut eller blacklistas.   


- Reflektera över säkerhet kontra användarupplevelse  

Säkerheten är prioriterad i min webbapplikation särskilt kring authentisiering och auktorisering samt skydd mot   
olika attacker. Detta för att skydda både användaren och data.  Genom att jag valt så hög säkerhets implementering   
blir det en kompromiss för användarupplevelsen som till exempel kontolåsning vid för många inloggningar,   
regelbunda refresh-tokens för att undvika utloggning, input validering med strikta regler. Eftersom jag vill hålla 
applikationen så säker som möjligt får användaren endast generiska svar och inte tydlig information på vad som   
gått fel.

