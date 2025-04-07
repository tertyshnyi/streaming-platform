# Dokumentácia Doménovej Vrstvy

### Architektúra: Porty a adaptéry (Hexagonálna architektúra)

Projekt využíva prístup **hexagonálnej architektúry (Ports and Adapters)**, ktorý zabezpečuje oddelenie doménovej logiky od implementačných detailov ako databáza, API alebo iné technológie.

#### Porty (rozhrania)

Porty sú **rozhrania definované v doméne**, ako napríklad:

- `MovieRepository` – rozhranie pre prácu s filmami a seriálmi
- `UserRepository` – správa používateľov
- `StreamingHistoryService` – história sledovania
- `RecommendationPort` – generovanie odporúčaní

Tieto rozhrania špecifikujú **čo doména potrebuje**, bez toho, aby určovali **ako** sa to má vykonať. Napr. doména vyžaduje načítať film podľa ID – ale implementácia môže byť pomocou SQL, NoSQL, REST API a pod.

#### Adaptéry (implementácie)

Adaptéry sú **konkrétne implementácie portov**, ktoré pripájajú doménu k vonkajšiemu svetu:

- relačné databázy (napr. cez JDBC, JPA)
- externé služby (napr. odporúčacie systémy)
- cache alebo iné zdroje dát
- 
