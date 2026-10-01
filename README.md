# OWL Swing App

Prosty przykład aplikacji okienkowej w Javie (Swing) wykorzystującej OWL API oraz reasoner HermiT.

Funkcje:
- Wczytywanie pliku .rdf/.owl
- Dodawanie instancji, data/object properties
- Uruchamianie reasonera HermiT
- Wyświetlanie instancji przypisanych do klasy

Uruchomienie (w terminalu devcontainer):

1) Skompiluj:

```bash
mvn -DskipTests compile
```

2) Uruchom klasę `App` z zależnościami:

```bash
mvn dependency:copy-dependencies
java -cp target/classes:target/dependency/* com.example.owlapp.App
```

Alternatywnie uruchom z IDE (np. IntelliJ/Eclipse) ustawiając `com.example.owlapp.App` jako główną klasę.

Jeśli masz gotowy plik `.rdf` i chcesz go otworzyć od razu przy starcie, podaj ścieżkę jako argument:

```bash
java -cp target/classes:target/dependency/* com.example.owlapp.App /pełna/ścieżka/do/pliku.rdf
```

Jeśli plik istnieje, aplikacja spróbuje go automatycznie wczytać przy uruchomieniu.
 
Instalacja i użycie HermiT / Pellet
----------------------------------

1) HermiT

- Pobierz JAR HermiT (np. ze strony projektu HermiT lub z zaufanego źródła). Jeśli artifact nie jest
	dostępny w Maven Central, zainstaluj go lokalnie do repozytorium Maven:

```bash
mvn install:install-file -Dfile=/pełna/ścieżka/hermit.jar \
	-DgroupId=org.semanticweb.hermit -DartifactId=hermit -Dversion=1.4.3.517 -Dpackaging=jar
```

- Po zainstalowaniu możesz dodać dependency do `pom.xml`:

```xml
<dependency>
	<groupId>org.semanticweb.hermit</groupId>
	<artifactId>hermit</artifactId>
	<version>1.4.3.517</version>
</dependency>
```


# zwi
Ontology of machine learning model selection

## Warsaw housing analysis MVP (nowy moduł)

Repozytorium zawiera również fundament MVP dla analizy rynku mieszkaniowego Warszawy w pakiecie:

`com.example.zwi`

Zakres MVP:
- transakcje RCN (import + normalizacja),
- model danych z rozdzieleniem `property` i `listing`,
- historia cen ofert (`listing_price_history`),
- filtrowanie transakcji,
- statystyki cen/m² (min/mediana/średnia/max/liczba),
- porównanie oferty z porównywalnymi transakcjami,
- markery mapy dla transakcji i ofert,
- rejestr polityk prawnych źródeł danych.

To jest warstwa domenowo-serwisowa gotowa do podpięcia pod API/GUI.
