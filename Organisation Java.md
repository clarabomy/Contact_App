# Projet Java 2
Développer répertoire en fenêtre pour le 10 mars (flex : 5j)

# Analyse du sujet

### Pré requis : 
- Projet Maven
- Appels MySQL
- Interface utilisateur graphique claire et efficace
- Code modularisé en plusieurs classes dédiées
- Fermer tous les flux ouverts (lecture / écriture en fichier / BDD)
- Code commenté répondant à des normes de programmation (à définir)
- Respecter points du cours :
> Types génériques (templates)  
> API collection (conteneurs)  
> MVC 

### Structure de BDD proposée (prof) : table « contacts » unique
- Nom 
- Prénom 
- Surnom
- numéro de téléphone
- adresse
- mail 
- date de naissance

### Minimum attendu :
- Lister tous les contacts de la base de données
- Ajouter un contact par formulaire
- Modifier un contact existant
- Supprimer un contact
- Exporter tous les contacts dans des fichiers vCard
- Tester les méthodes d’accès à la BDD par test unitaire

# Organisation d'équipe

### Répartition des rôles :
- Corentin : requêtes SQL + tests unitaires
- Clara : partie modèle (hors requêtes)
- Mathilde : partie vue et contrôleur (lien entre vue et modèle)

### Opérations court terme :
- Mise en accord sur les modules (améliorations) à développer (Clara ok)
- Définition de la base de données (Clara ok)
- Répartition des taches (Clara ok) 
- Définition des objectifs courts, moyen et long terme en accord avec l’équipe

### Objectifs du projet

**Court terme :**
- Esquisser le rendu visuel sur papier ou power point (Mathilde)
- Créer la bdd et le projet (Clara)
- Créer structure des requêtes préparées (Corentin)

**Moyen terme :**
- Créer le minimum requis pour l’affichage (Mathilde)
- Créer classes minimales requises pour le fonctionnement (Clara)
- Créer requêtes SQL + développer tests unitaires bdd (Corentin)

**Long terme :**
- Développer modules supplémentaires pour l’affichage (Mathilde)
- Développer modules supplémentaires pour le fonctionnement (Clara)
- Etendre tests unitaires au reste du projet (Corentin)
- Recherches vCards, images… (Corentin, Mathilde)


### Modules complémentaires (propositions)
*Attention - privilégier la qualité à la quantité*

Modules ajoutable avant développement : 
> Associer plusieurs numéros ou mails à un contact -> approuvé
>> Requêtes : table numéros et mails  
>> Affichage : formulaires & affichage dynamique

> Ajouter des notes à un contact -> approuvé
>> Requêtes : champ notes

> Regrouper des contacts (favoris, black & white list, pro…) -> approuvé
>> Requêtes : table category  
>> Affichage : selon la catégorie

Modules ajoutable en cours de développement : 
> Exporter ou supprimer seulement une partie des contacts en même temps -> à voir
>>Affichage : sélection des contacts (case à cocher ?)

> Chercher un contact -> approuvé (requête sql de site web)
>> Requêtes : requête associée  
>> Affichage : barre de recherche

Modules ajoutable en fin de développement :
> Trier les contacts par nom, prénom, surnom, mail, naissance… -> approuvé
>> Affichage : critère de tri  
>> Exécution : tri des données selon le critère

> Importer un ou plusieurs contacts sous forme de vCard -> à voir / secondaire
>> Exécution : décodage des vCard

> Ajouter une photo à un contact -> implique recherches donc à la fin
>> Exécution : gestion d’un fichier image + intégration aux vCard

### Structure de BDD adoptée : 
**Table « person »**
- Id (primaire)
- name
- surname 
- nickname
- adress 
- birth date 
- category_id

**Table « category »**
- Id (primaire)
- name

**Table « phone »**
- id (primaire)
- person_id
- phone
- type

**Table « mail »**
- id (primaire)
- Person_id
- Mail

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
