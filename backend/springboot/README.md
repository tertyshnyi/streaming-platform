# Dokumentácia: Modul springboot

Modul **springboot** je zodpovedný za zostavenie celej aplikácie a poskytuje runtime prostredie pre dependency injection (DI), ktoré je implementované pomocou **Spring Framework**. Tento modul je kľúčovou súčasťou aplikácie, ktorá zabezpečuje, aby jednotlivé komponenty a služby mohli spolupracovať na základe ich definovaných závislostí.

## Hlavné zodpovednosti modulu

1. **Zostavenie aplikácie**: Modul **springboot** zhromažďuje všetky komponenty, ktoré tvoria aplikáciu, a zabezpečuje ich spustenie.
2. **Dependency Injection**: Poskytuje runtime prostredie pre DI pomocou Spring Framework, čo umožňuje automatické vkladanie závislostí a tým zjednodušuje spravovanie závislostí medzi komponentami.
3. **Zostavenie Spring Beanov**: Zostavuje a spravuje **Spring beany** z doménových služieb a poskytuje im potrebné závislosti, ktoré sú adaptované v technologických vrstvách, napríklad repozitároch.

## Embedovaný Tomcat Server

Modul **springboot** využíva embedovaný **Tomcat server** ako runtime prostredie pre aplikáciu, čo je v predvolenej konfigurácii pre Spring Boot aplikácie. Vďaka embedovanému Tomcatu nie je potrebné, aby bol aplikáčný server konfigurovaný samostatne, čo zjednodušuje nasadenie a prevádzku aplikácie.

## Integrácia doménových služieb