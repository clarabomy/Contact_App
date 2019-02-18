#CategoryType
**protected**
short id
String name

**public**
constructeur
getters / setters
getCategoryList (requête sql)



# Classe Contact
**protected**
short id
String name
String surname
String nickname
String adress
Date birth
CategoryType category
sortedSet <String> mails
map <String, int> phones (string : étiquette)

**public**
constructeur / destructeur (containers)
getters / setters
hydratation : instanciation de la classe (map?)



# Classe ContactManager
**protected**
type bddFlux
ArrayList <Contact> people

**public**
constructeur / destructeur
getters / setters

addContact
getContact
getContactByCategory
getAllContact
deleteContact
exportContact
importContact (optionnel)


###################################

### BDD

addContact :
- entrée : instance contact
- exécution : requête add
- retours : confirmation d'enregistrement

getContact :
- entrée : nom, tel, mail (-> template)
- exécution : requête get et instancifie contact
- retours : instance contact

getContactByCategory :
- entrée : catégorie (par défaut : tous)
- exécution : requête get et mise en tableau
- retours : arrayList

getAllContact :
- entrée : tri (nom, téléphone, mail... -> template)
- exécution : requête get et mise en tableau
- retours : arrayList

deleteContact :
- entrée : nom, tel, mail (-> template)
- exécution : requête delete 
- retours : confirmation de suppression


### Fichiers

exportContact :
- entrée : instance contact
- exécution : crée fichier et le remplit s'il n'existe pas
- retours : confirmation de création

importContact : optionnel
- entrée : nom fichier ?
- exécution : lit fichier et instancifie contact
- retours : instance contact
