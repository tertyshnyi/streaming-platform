# Dokumentácia Doménovej Vrstvy

## Prehľad

Doménová vrstva v našej architektúre nasleduje princípy hexagonálnej architektúry (tiež známej ako architektúra portov a adaptérov). To znamená, že doménová vrstva zostáva nezávislá od akejkoľvek konkrétnej technológie alebo frameworku, čo zabezpečuje čisté a ľahko udržateľné jadro s minimálnymi závislosťami.

## Hexagonálna Architektúra v Doméne

Doména neobsahuje žiadne závislosti na technologických stackoch alebo frameworkoch. Jej hlavnou úlohou je definovať biznis logiku v čo najčistejšej forme.

Doména poskytuje porty, ktoré sú v našom prípade definované ako rozhrania (interfaces). Tieto rozhrania predstavujú vonkajšie hranice domény a umožňujú interakciu s okolitými systémami a službami.

## Porty v Našom Systéme

Porty sú definované ako rozhrania ako napríklad `DiscussionMessageRepository` alebo `UserRepository`. Tieto rozhrania určujú, ako sa doména spája s okolitým svetom bez špecifikácie implementačných detailov.

Definovaním týchto rozhraní doména určuje, aké operácie očakáva (napr. nájdenie používateľa alebo uloženie diskusného príspevku), bez toho, aby vedela, ako budú tieto operácie vykonané.

Táto abstrakcia poskytuje flexibilitu rôzne technológie môžu byť použité na implementáciu týchto rozhraní (napr. použitie relačnej databázy, NoSQL alebo dokonca externých služieb).


## Čistý Kód Domény

Okrem portov pre repozitáre zostáva doména čistá a technologicky agnostická. Nie sú povolené žiadne frameworky alebo knižnice okrem SLF4J pre logovanie, čím sa zabezpečuje, že biznis logika je nezávislá od infraštruktúry a technologických obmedzení.

Logovanie pomocou SLF4J je v doménovej vrstve povolené, aby bolo možné sledovať dôležité udalosti a zabezpečiť transparentnosť bez ohrozenia nezávislosti domény.


