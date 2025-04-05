# Dokumentácia: Modul inbound-controller-ws

Modul **inbound-controller-ws** slúži na vystavenie RESTful endpointov nad aplikáciou a predstavuje technologickú vrstvu, ktorá umožňuje prístup do doménového modulu prostredníctvom určeného frameworku.

Modul **inbound-controller-ws** obsahuje vlastný model, ktorý je definovaný prostredníctvom OpenAPI špecifikácie.

## Závislosti modulu

Tento modul má závislosti na:
- **domain**: zabezpečuje vstup do doménovej logiky.
- **api-spec**: obsahuje špecifikáciu REST modelov, definovanú prostredníctvom OpenAPI.

## Implementácia

Pre implementáciu REST servera používame **Spring Web**, ktorý poskytuje základné nástroje na vytváranie REST endpointov spolu s ďalšími časťami na podporu HTTP komunikácie. Ak je potrebné zabezpečenie, konfigurujeme modul pomocou **Spring Security** pre správu autentifikácie a autorizácie, čo nám umožňuje kontrolovať prístup k jednotlivým endpointom podľa potrieb aplikácie.

