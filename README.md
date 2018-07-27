# GeoReporter
Istruzioni per l'istallazione del software

## Descrizione del Software
Il sofware, sviluppato nell ambito del progetto Geopartner, offre la funzionalita' di importare i dati provenienti da differenti basi dati e in differenti formati e caricarli in un knowledge store secondo uno schema ontologico.
I dominio dell applicazione e' quello dei dati catastali e dei vari tributi, utenze e contratti associati alle Unita' Immobiliari
Il spftware e' formato da 2 componenti principali: GeoreporterWrappers e GeoreporterService.

### GeoreporterWrappers
Implementa le funzionalita' di import dei dati da formati csv,  xst in una ontologia. La corrispondenza tra il contenuto dei dati e L'ontologia viene effettuata utilizzando una serie di file di mappings

### GeoreporterService
Implementa un insieme di servizi rest che si collegano alla knowlwedge base e effettuano l'inserimento vero e proprio dei dati e permettono poi di recuperarli 


## Prerequisiti

-Java 1.8
-Tomcat 8
-springles

## Utilizzo dei wrappers:
I file Mapping sono dei file in formato json.
Definiscono la corrispondenza tra i dati che vengono importati e la loro destinazione al interno del ontologia
{"mapping":"http://dkm.fbk.eu/georeporter#renditaEuro","nome":"http://dkm.fbk.eu/georeporter#renditaeuro","tipo":"http://www.w3.org/2001/XMLSchema#float"},

mapping: definisce il nome della destinazione del dato nel'ontologia
nome: definisce in nome della sorgente del dato nel file di importazione
tipo: definisce il tipo di dato

## Importazione dei dati via java
ImportGeoreporter.java e' La classe che contiene i metodi per importare i vari dati
I dati di esempio  per il comune di Trambileno si trovano nella directory
/file/Trambileno

per ciascun file dei dati, bisogna fornire il corrispettivo file dei mappings
per alcuni dati che non contengono le intestazioni dei dati, bisogna fornire un file degli header (il nome dei campi delle tabelle) al fine di poter effettuare correttamente il mapping

Nel metodo main vengono forniti degli esempi di come effettare le chiamate per fare l'import

## Importazione dei dati via web
Oltre che tramite la classe ImportGeoreporter,java, e' possibile fare l'importe dei dati attraverso un interfaccia web accedendo all' indirizzo:
http://<nome_del_server:porta>/GeoreporterService/tools/import.html



