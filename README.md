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
HTML sanitizer, JPA, Att databasfrågor att skrivna på rätt sätt.  

Man in the middle  
Hot- Data manipuleras eller avlyssnas genom överföring.  
Skydd- Konfigurerat i securityConfig.  
  
Clickjacking  
Hot- Angripare gömmer en dold ifram i appen och lurar användaren att klicka på något de inte ser.  
Skydd- X-frame- options är satt till deny i securityconfig.  
  
  
- Beskriv säkerhetsarkitekturen och designbeslut  
    

  

- Analysera kvarvarande säkerhetsrisker och begränsningar
- Reflektera över säkerhet kontra användarupplevelse  


