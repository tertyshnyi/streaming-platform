# Dokumentácia: Modul inbound-controller-ws

Modul `inbound-controller-ws` slúži na **vystavenie RESTful endpointov** nad aplikačnou logikou a predstavuje **technologickú vrstvu** (tzv. inbound adaptér), ktorá umožňuje vonkajším systémom a používateľom komunikovať s doménou cez HTTP protokol.

#### Popis

Tento modul sprostredkúva prístup do jadra aplikácie pomocou REST API a zabezpečuje transformáciu dát medzi:

- externým svetom (napr. webové alebo mobilné rozhrania),
- a internou doménovou vrstvou aplikácie.

REST modely, ktoré tento modul využíva, sú **definované prostredníctvom OpenAPI špecifikácie**. To umožňuje jasne a zrozumiteľne dokumentovať dostupné endpointy a generovať klientov pre rôzne platformy.

#### Závislosti modulu

Modul závisí od nasledujúcich komponentov:

- `domain` – poskytuje vstupné porty (rozhrania) pre volanie doménovej logiky.
- `api-spec` – obsahuje dátové štruktúry (DTO) a OpenAPI definície používané v REST API.

#### Implementácia

Na implementáciu REST servera používame **Spring Web**, ktorý poskytuje všetky potrebné nástroje na:

- tvorbu REST controllerov,
- spracovanie HTTP požiadaviek a odpovedí,
- prácu s dátovými modelmi.

V prípade potreby zabezpečenia endpointov modul integruje **Spring Security**, ktorý umožňuje:

- konfiguráciu autentifikácie a autorizácie,
- ochranu jednotlivých častí API na základe rolí alebo oprávnení používateľa,
- využitie JWT tokenov alebo iných mechanizmov na správu prístupu.

#### Výhody

- Jasne oddelená zodpovednosť REST vrstvy.
- Flexibilná implementácia na základe OpenAPI.
- Možnosť generovania dokumentácie a klientov automaticky.
- Bezpečný prístup k doméne cez štandardné HTTP mechanizmy.
