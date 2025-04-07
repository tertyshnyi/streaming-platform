# Dokumentácia: Modul outbound-repository-jpa

Modul `outbound-repository-jpa` slúži na **technickú adaptáciu repozitárov z doménovej vrstvy**, a tým **zabezpečuje premostenie medzi doménou a perzistenčnou technológiou**. Tento modul implementuje tzv. outbound adaptér, ktorý zapúzdruje detaily databázového prístupu pomocou **Spring Data JPA**.

#### Zámerná ochrana domény

Jedným z hlavných cieľov tohto modulu je zachovanie **čistej a nezávislej doménovej logiky**, ktorá **neobsahuje žiadne technologické závislosti**. Doména definuje len **porty (rozhrania)**, ktoré špecifikujú, aké operácie perzistencie očakáva – napríklad:

- `UserRepository` – pre správu používateľov,
- `MovieRepository` – pre správu filmov,
- `WatchHistoryRepository` – pre históriu sledovania používateľov.

Modul `outbound-repository-jpa` následne tieto porty implementuje pomocou Spring Data JPA, čo umožňuje efektívnu a deklaratívnu prácu s databázou.

#### Izolácia cez balíkovú viditeľnosť

Pre ešte **vyššiu izoláciu a bezpečnosť domény** sú technické rozhrania ako napríklad `UserSpringDataRepository` alebo `MovieSpringDataRepository` **viditeľné len v rámci balíka** (`package-private`). Týmto spôsobom zabezpečujeme, že tieto implementácie **nemôžu byť priamo použité mimo modulu**, a tým sa zachováva čistota doménového API.

#### Implementácia

Modul využíva:

- **Spring Boot Data JPA** na automatickú implementáciu repozitárov na základe rozhraní,
- **Entity triedy**, ktoré reprezentujú perzistentné dáta (napr. `UserEntity`, `MovieEntity`, `WatchEntryEntity`),
- **Mapovanie medzi doménovým a databázovým modelom**, najčastejšie pomocou `Mapper` tried.

Týmto spôsobom sa zabezpečuje, že:

- databázové operácie (CRUD) sú efektívne a robustne realizované,
- doména zostáva nezávislá od technológií ako JPA, Hibernate, či samotná databáza,
- architektúra systému je rozšíriteľná – napr. v budúcnosti môžeme JPA vymeniť za iný perzistenčný mechanizmus bez zásahu do doménovej vrstvy.

#### Výhody

- Čisté oddelenie technológie od biznis logiky.
- Možnosť jednoducho meniť perzistentnú technológiu (napr. z JPA na MongoDB).
- Jednoduchšia testovateľnosť domény bez potreby databázy.
- Deklaratívne repozitáre cez Spring Data (bez písania SQL).
