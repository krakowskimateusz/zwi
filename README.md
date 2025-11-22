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

2) Openllet / Pellet

- Openllet to aktywny fork Pellet — często dostępny w Maven Central (np. `openllet-core`). Jeśli
	użyjesz Openllet, dodaj odpowiednie dependency do `pom.xml` (wersję sprawdź na Maven Central).
- Jeśli chcesz użyć oryginalnego Pellet, pobierz JAR i zainstaluj go lokalnie analogicznie do HermiT.

3) Co zmieniłem w kodzie

- `OntologyManager.runReasoner()` próbuje automatycznie wykryć i użyć dostępnego reasonera w tej
	kolejności: HermiT, Openllet, Pellet, ELK. Jeśli żaden nie jest dostępny, używany jest wbudowany
	`StructuralReasoner` z OWLAPI (fallback) — dzięki temu aplikacja działa od razu bez dodatkowych
	zależności, ale można uzyskać silniejsze wnioski po dodaniu HermiT/Openllet/Pellet.

Jeśli chcesz, mogę dodać do `pom.xml` przykładowe, odkomentowane dependency dla Openllet, lub przeprowadzić
Cię krok po kroku przez instalację HermiT/Pellet (pobranie JAR i instalacja lokalna). 
# zwi
Ontology of machine learning model selection
