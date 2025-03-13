# Názov témy
Streamingová platforma

## Stručný popis témy
Táto služba poskytuje používateľom možnosť vyhľadávať filmy a televízne seriály podľa rôznych parametrov, prezerať ich v rôznej kvalite v závislosti od predplatného a zanechávať komentáre. Zavedený je systém rolí: administrátori môžu pridávať a spravovať obsah a používatelia môžu s platformou komunikovať. K dispozícii je registrácia, autorizácia, vyhľadávanie, história prehliadania a pohodlný systém kategórií. Platforma podporuje rôzne formáty videí, čím zabezpečuje pohodlné sledovanie pre každého používateľa.


## Zoznam požiadaviek
- **RQ01**  Systém umožní registráciu používateľa. (The system allows user registration.)
- **RQ02**  Systém umožní autentifikáciu používateľa. (The system allows user authentication.)
- **RQ03**  Systém umožní prehrávanie filmu. (The system allows watching a film.)
- **RQ04**  Systém umožní úpravu používateľského profilu. (The system allows editing the user profile.)
- **RQ05**  Systém umožní pridanie komentára k filmu. (The system allows commenting on a film.)
- **RQ06**  Systém umožní zobrazenie histórie sledovania. (The system allows viewing watch history.)
- **RQ07**  Systém umožní správu nastavení prehrávača. (The system allows managing player settings.)
- **RQ08**  Systém umožní správu používateľov. (The system allows managing users.)
- **RQ09**  Systém umožní nahranie filmu. (The system allows uploading a film.)
- **RQ10**  Systém umožní úpravu informácií o filme. (The system allows editing film information.)
- **RQ11**  Systém umožní vymazanie komentárov. (The system allows deleting comments.)
- **RQ12**  Systém umožní zablokovanie používateľov. (The system allows banning users.)
- **RQ13**  Systém umožní nahlásenie komentára. (The system allows reporting a comment.)
- **RQ14**  Systém umožní hodnotenie filmu. (The system allows rating a film.)
- **RQ15**  Systém umožní nahlásenie problému s prehrávačom podpore. (The system allows reporting a player issue to support.)

Pre lepšie pochopenie prípadov použitia si môžete pozrieť súbor Use-cases.pdf. (Verzia v angličtine.)

## Slovník pojmov
| **Pojem**                 | **Anglický názov**     | **Definícia**  |
|---------------------------|------------------------|----------------|
| **Používateľ**              | **User**               | Registrovaný a prihlásený používateľ, ktorý môže sledovať filmy a pridávať komentáre. |
| **Moderátor**               | **Moderator**          | Používateľ s oprávneniami na správu komentárov. Môže mazať komentáre, blokovať a odblokovať používateľov. |
| **Administrátor**           | **Admin**              | Používateľ s oprávneniami na správu obsahu platformy. Môže pridávať a upravovať filmy. |
| **Boh**                     | **God**                | Najvyššia úroveň oprávnení. Môže spravovať používateľov (pridávať moderátorov a administrátorov) a vykonávať všetky akcie moderátorov a administrátorov. |
| **Rola**                   | **Role**               | Definuje úroveň prístupu používateľa (`USER`, `MODERATOR`, `ADMIN`, `GOD`). |
| **Žáner**                  | **Genre**              | Kategória filmu, napríklad `ACTION`, `DRAMA`, `COMEDY`, `THRILLER`, `DOCUMENTARY`, `HORROR`. |
| **Film**                    | **Film**               | Audiovizuálny obsah dostupný na sledovanie pre používateľov. |
| **Prehrávač**               | **Player**             | Softvérová súčasť systému, ktorá umožňuje prehrávanie filmov. |
| **Komentár**                | **Comment**            | Textová recenzia, ktorú používateľ zanechal pod filmom. |
| **Hlásenie**                | **Report**             | Sťažnosť na komentár, ktorá obsahuje dôvod a stav preskúmania. |
| **ReportStatus**            | **ReportStatus**       | Stav hlásenia, ktorý určuje aktuálny stav spracovania hlásenia. Môže byť PENDING, REJECTED, alebo APPROVED. |
| **ReportType**            | **ReportType**       | Typ hlásenia, ktorý určuje dôvod podania sťažnosti na komentár. Môže byť OFFENSIVE LANGUAGE, HATE SPEECH, alebo SPAM. |