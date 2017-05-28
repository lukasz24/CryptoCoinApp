# CryptoCoinApp

Aplikacja CryptoCoinApp jest przeznaczona dla urządzeń z systemem Android w wersji 4.0 lub wyższej. Jest ona bazą informacji o 20 najpopularniejszych obecnie kryptowalutach na rynku.
Dostępne funkcjonalności:
  •	Wybór kryptowalut, które chcemy śledzić – możliwe obserwowanie ich kursu, oraz zmian tegoż kursu.
  •	Portfel, do którego można dodawać oraz odejmować posiadane kryptowaluty – stanowi to jedynie funkcję poglądową (aplikacja nie umożliwia zakupu/sprzedaży kryptowalut).
  •	Możliwość obserwowania zsumowanej wartości wszystkich kryptowalut zgromadzonych w portfelu oraz śledzenie zmiany tej wartości.
  •	Aplikacja posiada możliwość wyboru jednej z 3 walut tradycyjnych (USD, EUR oraz PLN), na które będą przeliczane wartości kryptowalut.
  •	Istnieje możliwość ustalenia hasła dostępu do portfela.
  •	Standardowa możliwość kontaktu z autorami przez wysłanie wiadomości e-mail, aby przekazać ewentualne uwagi, napotkane problemy.

## Opis wyglądu aplikacji

Aplikacja składa się z kilku aktywności. Na samym początku użytkownik ma do wyboru 3 przyciski (rynek, portfel oraz autorzy). Istnieje także możliwość skorzystania z menu, które przekierowuje użytkownika do ustawień.
Aktywności:
  •	Aktywność główna (jak wspomniano zawiera w sobie 3 przycisku odsyłające do innych aktywności oraz menu rozsuwane)
  •	Rynek – umożliwia dodawanie kryptowalut, które pojawiają się w formie listy. Użytkownik może wybrać kryptowaluty, których kurs oraz zmianę kursu chce poznać oraz obserwować. Po kliknięciu w daną pozycję na liście zostajemy przekierowani do ekranu z informacjami na jej temat.
  •	Szczegóły o kryptowalutach – aktywność uruchamia się z poziomu rynku po kliknięciu na wybraną kryptowalutę. Zawiera informacje o niej.
  •	Portfel – dostęp do tej aktywności może być nieograniczony lub ograniczony hasłem zdefiniowanym w ustawieniach. W tej aktywności znajdują się przyciski odpowiadające za dodawanie/odejmowanie kryptowalut. Wprowadzane informacje pojawiają się na ekranie jako pola tekstowe. Znajduje się tam także pole zawierające podsumowanie wartości całego portfela. Waluta w jakiej będzie wyrażone to podsumowanie może być zadeklarowana w ustawieniach.
  •	Autorzy – krótkie informacje o autorach oraz możliwość wysłania wiadomości e-miał z uwagami.
  •	Ustawienia – miejsce, w którym użytkownik ma możliwość zdefiniowania wspomnianej waluty, w której chce obserwować wartości kryptowalut oraz portfela, a także ma możliwość ustawienia hasła dostępu do portfela (wybór opcji za pomocą switcha).

## Szczegółowy opis

### Klasy wchodzące w skład aplikacji:

1.	AdapterListy
2.	Autorzy
3.	DatabaseHandler
4.	Krypto
5.	KryptoDAO
6.	Kryptos
7.	ListaWalut
8.	MainActivity
9.	Portfel
10.	ShowDetailsActivity
11.	Ustawienia


Aplikacja wykorzystuje do działania bazę danych, preferencje oraz zapis do pliku. Wymienione wyżej klasy służą do obsługi aktywności lub wspomnianej bazy danych. 
Do stworzenia graficznej oprawy wykorzystano wiele layoutów (różne rodzaje).

## Opis wybranych klas

Klasy DatabaseHandler, KryptoDAO, Kryptos są związane z przechowywaniem informacji pobranych za pomocą API (https://www.cryptonator.com/api/) w bazie danych oraz umożliwiają dostęp do nich w aktywnościach.

Aktywność główna

Zawiera w sobie 3 przyciski odsyłające do innych aktywności oraz rozsuwane menu, które pozwala na przejście do ustawień. Jeśli użytkownik zadeklaruje hasło w ustawieniach to po wciśnięciu przycisku kierującego do Portfela wyświetla się okno dialogowe proszące o podanie tegoż hasła. Jeśli zostanie wprowadzone błędne hasło wyświetla się odpowiedni komunikat, a aplikacja wraca do widoku aktywności głównej. Jeśli hasło będzie poprawne użytkownik trafia do aktywności Portfel.

### Rynek

Aktywność ta składa się z przycisku, który otwiera okno z listą 20 kryptowalut, które można zaznaczyć jako obserwowane. Po zaznaczeniu jakiejś (lub kilku opcji) i potwierdzeniu tego wyboru informacje o danych kryptowalutach pojawiają się na ekranie w formie listy. Można tam zobaczyć logo, nazwę kryptowaluty oraz jej wartość i zmianę tej wartości. Po kliknięciu w daną pozycję na liście otwiera się aktywność, która wyświetla informacje o danej kryptowalucie.

### Portfel

Zawiera w sobie dwa przyciski pozwalające dodać lub odjąć kryptowalutę z portfela. Po dodaniu jakiejś waluty informacje o niej (nazwa, ilość sztuk, wartość) pojawia się na ekranie. Użytkownik może także obserwować informację dotyczącą podsumowania wartości portfela, które to zmienia się dynamicznie w zależności od dodawanych/odejmowanych sztuk kryptowalut.

### Autorzy

Aktywność ta składa się z elementów ułatwiających graficzny rozkład takich jak: TableLayout, imageview, textView oraz jednego przycisku, który to odpowiada za inicjację zdarzenia odpowiedzialnego za wysyłanie wiadomości e-mail. 

### Ustawienia

Jest to aktywność, która zawiera w sobie dwa Spinnery umożlwiające wybór waluty oraz czasu odświeżania. Znajduje się tam także Switch, który aktywuje (lub dezaktywuje) pola tekstowe służące do wprowadzania hasła lub jego zmieniania. Ostatnimi elementami są dwa przyciski, które odpowiadają albo za potwierdzenie wprowadzonych zmian, albo za ich anulowanie oraz powrót do aktywności głównej.

## Sposób obsługi

Użytkownik korzystając z menu-ustawienia może wybrać preferowaną walutę oraz ustawić hasło do portfela, a także wybrać próg zmiany kursu waluty, przy którym mają wyświetlać się stosowne komunikaty. 
Wybór walut do obserwowania w rynku odbywa się za pomocą zaznaczenia/odznaczenia wybranych walut za pomocą checboxów w liście pojawiającej się po naciśnięciu przyciska „dodaj walutę” oraz potwierdzeniu wyboru przyciskiem „ok”.
Wejście do portfela jest możliwe bez ograniczeń gdy nie zostało zdefiniowane hasło lub po jego wpisaniu w oknie pojawiającym się po naciśnięciu przycisku „Portfel” w ekranie początkowym.
Okno zawierające informacje o autorach zawiera w sobie przycisk „kontakt z autorami”, który otwiera aplikację służącą do wysyłania e-maili, wypełnia tytuł wiadomości oraz adres na jaki ma być ona wysłana.
Zmiany w portfelu dokonuje się za pomocą dwóch przycisków- „dodaj walutę” oraz „odejmij walutę”. Oba wywołują okno dialogowe pozwalające wybrać z rozwijanej listy nazwę kryptowaluty oraz wpisać w polu do tego przeznaczonym ilość sztuk (format liczbowy, double).
Pole wyświetlające sumę wartości portfela zmienia kolor tekstu w zależności od tego czy od ostatniej wizyty kursy walut w nim zgromadzonych wzrosły (zielony) lub zmalały (czerwony). Pojawia się także stosowne powiadomienie informujące o zmianie % wartości.
Kliknięcie na wybraną walutę w aktywności Rynek powoduje przejście do szczegółów z nią związanych- krótki opis oraz informacje o zmianach kursów.
Do pobrania aktualnych kursów walut i poprawnego działania aplikacji potrzebne jest połączenie z Internetem.

## Autorzy

Radosław Smyksy 188182
Łukasz Pudzisz 187924
