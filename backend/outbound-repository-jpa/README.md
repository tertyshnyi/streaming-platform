# Dokumentácia: Modul outbound-repository-jpa

Modul **outbound-repository-jpa** slúži na technickú adaptáciu repozitárov z doménovej vrstvy, čím zabezpečuje, že samotná doména nepozná technologické detaily perzistencie. Doména poskytuje len porty v podobe rozhraní, ako napríklad `DiscussionMessageRepository` alebo `UserRepository`. Tento modul zabezpečuje implementáciu perzistencie, pričom využíva Spring Boot Data na realizáciu perzistentných operácií.


## Zámerná ochrana domény

Hlavnou úlohou modulu **outbound-repository-jpa** je zabezpečiť, aby doménová vrstva bola odtienená od technologických detailov, akými sú konkrétne perzistenčné nástroje a implementácie. Tento prístup zabezpečuje, že  operácie, ktoré systém potrebuje, sú definované a riadené doménovou logikou a nie technologickým frameworkom.

Pre ešte väčšiu izoláciu má rozhranie `PersonSpringDataRepository` nastavenú viditeľnosť na úrovni balíka (package visibility), čím sa minimalizuje jeho prístupnosť a vplyv na zvyšok systému.

![outbound JPA](outbound.png)