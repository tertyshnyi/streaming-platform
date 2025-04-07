# Dokumentácia: Modul springboot

Modul `springboot` je **zodpovedný za zostavenie celej aplikácie** a poskytuje **runtime prostredie pre dependency injection (DI)**, ktoré je implementované pomocou **Spring Framework**. Tento modul je kľúčovou súčasťou aplikácie, ktorá zabezpečuje, že jednotlivé komponenty a služby môžu **efektívne spolupracovať** na základe ich definovaných závislostí.

#### Hlavné zodpovednosti modulu

1. **Zostavenie aplikácie**: Modul `springboot` zhromažďuje všetky komponenty, ktoré tvoria aplikáciu, a zabezpečuje ich spustenie. Tento proces je automatizovaný pomocou Spring Boot, čo uľahčuje vývoj a nasadenie aplikácie.

2. **Dependency Injection (DI)**: Modul poskytuje **runtime prostredie pre DI**, čím umožňuje **automatické vkladanie závislostí** medzi komponentmi aplikácie. To zjednodušuje správu závislostí medzi službami, ako sú doménové služby, repozitáre, bezpečnostné komponenty, a ďalšie technologické vrstvy aplikácie.

3. **Zostavenie Spring Beanov**: Modul `springboot` vytvára a spravuje **Spring beany**, ktoré reprezentujú doménové služby ako `UserService`, `MovieService`, alebo `StreamingService`. Tieto beany sú následne poskytované ďalším komponentom aplikácie, ktoré ich používajú na vykonávanie obchodnej logiky a interakciu s inými službami.

#### Embedovaný Tomcat Server

Modul `springboot` využíva **embedovaný Tomcat server** ako runtime prostredie pre aplikáciu. Tomcat je prednastavený server v rámci Spring Boot aplikácií, čo znamená, že **nie je potrebné samostatne konfigurovať aplikačný server**. Tento embedovaný prístup výrazne **zjednodušuje nasadenie a prevádzku aplikácie**, pretože všetky potrebné komponenty sú integrované a pripravené na spustenie priamo z balíka aplikácie.

#### Integrácia doménových služieb

V rámci modulu `springboot` sú **doménové služby** (ako napríklad **`UserService`**, **`MovieService`**) integrované s technologickými vrstvami (napr. repozitáre pre filmy a používateľov), čo umožňuje aplikácii vykonávať operácie ako:

- Registrácia používateľov,
- Správa filmových zoznamov.

Týmto spôsobom Spring Boot zabezpečuje, že všetky komponenty sú **jednoducho prístupné** a správne prepojené, čím sa uľahčuje vývoj a údržba aplikácie.

#### ✅ Výhody modulu `springboot`

- **Zjednodušené nasadenie aplikácie** vďaka embedovanému Tomcat serveru.
- **Automatické spravovanie závislostí** medzi komponentmi pomocou Dependency Injection.
- **Flexibilita a rozšíriteľnosť**, keďže Spring Boot uľahčuje implementáciu nových funkcií a technológií bez potreby zásahov do základnej aplikácie.
- **Rýchly vývoj** aplikácie s možnosťou okamžitého testovania a nasadenia vďaka Spring Boot konfigurácii.
