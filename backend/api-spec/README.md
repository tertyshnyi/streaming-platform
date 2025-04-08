# Dokumentácia API pre používateľov

Toto je API pre používateľov pre streamingovú platformu, ktoré umožňuje spravovať používateľov (vytvoriť, aktualizovať, odstrániť, získať informácie o používateľovi).

## Prehľad API

Toto API poskytuje nasledujúce operácie na správu používateľov:

- **Vytvoriť nového používateľa**
- **Získať používateľa podľa ID**
- **Aktualizovať používateľa podľa ID**
- **Odstrániť používateľa podľa ID**

### Verzia API
- Verzia: 1.0.0

---

## Endpoints

### 1. Vytvoriť nového používateľa

**POST** `/users`

Tento endpoint umožňuje vytvoriť nového používateľa. Je potrebné poskytnúť objekt `CreateUserRequest` v tele požiadavky.

#### Parametre požiadavky

- `name`: Meno používateľa
- `email`: Email používateľa
- `password`: Heslo používateľa
- `phoneNumber`: Telefónne číslo používateľa
- `authorities`: Zoznam rolí (môže byť `USER`, `RELEASER`, `MODERATOR`, `ADMIN` alebo `null`.)
- `profileImg`: URL obrázku profilu používateľa

#### Odpoveď

**201 Vytvorené**

Vracia vytvoreného používateľa s jeho informáciami.

---

### 2. Získať používateľa podľa ID

**GET** `/users/{id}`

Tento endpoint umožňuje získať podrobnosti o používateľovi podľa ID.

#### Parametre

- `id`: ID používateľa (Long)

#### Odpoveď

**200 OK**

Vracia informácie o používateľovi.

**404 Nenájdené**

V prípade, že používateľ s daným ID neexistuje.

---

### 3. Aktualizovať používateľa

**PUT** `/users/{id}`

Tento endpoint umožňuje aktualizovať údaje o používateľovi podľa ID.

#### Parametre

- `id`: ID používateľa (Long)

#### Parametre požiadavky

- `name`: Meno používateľa
- `email`: Email používateľa
- `phoneNumber`: Telefónne číslo používateľa
- `authorities`: Zoznam rolí používateľa
- `profileImg`: URL obrázku profilu používateľa
- `createdAt`: Dátum a čas vytvorenia používateľa

#### Odpoveď

**200 OK**

Vracia aktualizované informácie o používateľovi.

**404 Nenájdené**

V prípade, že používateľ s daným ID neexistuje.

---

### 4. Odstrániť používateľa

**DELETE** `/users/{id}`

Tento endpoint umožňuje odstrániť používateľa podľa ID.

#### Parametre

- `id`: ID používateľa (Long)

#### Odpoveď

**204 Bez obsahu**

Používateľ bol úspešne odstránený.

**404 Nenájdené**

V prípade, že používateľ s daným ID neexistuje.