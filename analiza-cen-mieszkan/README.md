# analiza-cen-mieszkan

Gotowy do skopiowania projekt MVP do analizy cen mieszkań w Warszawie.

## Co zawiera

- warstwową architekturę (`ingestion`, `domain`, `repository`, `service`, `api`, `compliance`),
- importer i normalizację danych transakcyjnych RCN (CSV),
- model danych: `property`, `listing`, `listing_price_history`, `transaction`,
- filtrowanie i statystyki cen transakcyjnych,
- porównanie oferty do podobnych transakcji (comps),
- markery mapy dla transakcji i ofert,
- testy jednostkowe kluczowej logiki.

## Uruchomienie testów

```bash
mvn test
```

## Jak skopiować do nowego repo

Skopiuj całą zawartość folderu `analiza-cen-mieszkan/` do nowego repozytorium `analiza-cen-mieszkan`.
