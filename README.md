# Projet Java 2 : Contact app
Développer répertoire en fenêtre pour le 10 mars (flex : 5j)

# Analyse du sujet

### Pré requis : 
- Projet Maven
- Interface utilisateur graphique claire et efficace
- Code modularisé en plusieurs classes dédiées, commentées et claires
- Fermer tous les flux ouverts (lecture / écriture en fichier / BDD)
- Utiliser points du cours : templates, conteneurs, MVC & DAO

### Minimum attendu :
- Lister tous les contacts de la base de données
- Ajouter un contact par formulaire
- Modifier un contact existant
- Supprimer un contact
- Exporter tous les contacts dans des fichiers vCard
- Tester les méthodes d’accès à la BDD par test unitaire

# Organisation d'équipe

### Répartition des rôles :
- Corentin : tests unitaires + flux fichiers
- Clara : partie modèle et requêtes
- Mathilde : partie vue et contrôleur (complémentaires)

### Objectifs du projet

**Court terme :**
- Esquisser le rendu visuel sur power point (Mathilde)
- Créer la bdd et le projet, réfléchir aux classes du modèle (Clara)
- Recherches sur les vCards (Corentin)

**Moyen terme :**
- Développer affichage "minimum attendu" (Mathilde)
- Développer fonctionnalités "minimum attendues" (Clara)
- Développer exportation en vCard + tests unitaires bdd ? (Corentin)

**Long terme :**
- Développer modules supplémentaires pour l’affichage (Mathilde)
- Développer modules supplémentaires pour le fonctionnement (Clara)
- Développer importation de vCard + Etendre tests unitaires au reste du projet ? (Corentin)


### Modules complémentaires (propositions)
*Attention - privilégier la qualité à la quantité*

Modules ajoutable avant développement (impact bdd) : 
> Associer plusieurs numéros ou mails à un contact
>> Abandon : trop complexe avec le modèle DAO  

> Ajouter des notes à un contact
>> Implémenté : ajout d'un champ "notes" en BDD  

> Catégoriser les contacts -> approuvé
>> Implémenté : ajout d'une table "category", regroupements par catégorie possible

Modules ajoutable en cours de développement : 
> Exporter ou supprimer seulement une partie des contacts en même temps
>> Réalisable ? sélection des contacts  

> Chercher un contact
>> Réalisable ? barre de recherche en vue, requête associée  

Modules ajoutable en fin de développement :
> Trier les contacts par nom, prénom, surnom, mail, naissance… -> approuvé
>> Réalisable ? critère de tri en vue, requête associée  

> Importer un ou plusieurs contacts sous forme de vCard (secondaire)
>> Réalisable ? récupération de ficier en vue, décodage des vCard  

> Ajouter une photo à un contact (nécessite recherches)
>> Réalisable ? gestion d’un fichier image, intégration aux vCard

### Structure de BDD adoptée : 
**Table « person »**
- id (primaire)
- lastname
- firstname 
- nickname
- phone
- id_category
- email
- adress 
- birthday 
- notes

**Table « category »**
- id (primaire)
- name

### Arborescence fichiers
> Src  
>> Tests  
>>> Db.dao  
>>>> **Test des classes du dossier associé**  

>>> Ressources  
>>>> **Test des classes du dossier associé**  

>> Main  
>>> Ressources  
>>>> Fxml files (vérifier nom)  
>>>>> **Fichiers de vue**  

>>>> SQL  
>>>>> **Script bdd**  

>>> Java  
>>>> Contact_app  
>>>>> **Main (lanceur du projet)**  

>>>> Services  
>>>>> **En lien avec la fenêtre et son contenu (confirmation Mathilde ?)**  

>>>> View  
>>>>> **controleurs associés au fichiers fxml (confirmation Mathilde ?)**  

>>>> Model  
>>>>> Db.dao  
>>>>>> **Connexion et requêtes à la bdd**  

>>>>> Db.entities  
>>>>>> **Entitées (classes matérialisant les tables)**  

>>>>> File.manager (vérifier nom)  
>>>>>> **Gestion des fichiers**  
