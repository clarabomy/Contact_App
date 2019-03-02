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
- Corentin : flux fichiers
- Clara : partie modèle et requêtes + tests unitaires associés
- Mathilde : partie vue et contrôleur (lien entre vue et modèle) + tests unitaires associés (facultatif)

### Objectifs du projet

**Première étape :**
- Esquisser le rendu visuel (Mathilde) - ok
- Créer la bdd et le projet, réfléchir aux classes du modèle (Clara) - ok
- Recherches sur les vCards (Corentin) - ?

**Seconde étape :**
- Développer affichage "minimum attendu" (Mathilde) - vue ok, contrôleurs ?
- Développer fonctionnalités "minimum attendues" + tests unitaires BDD (Clara) - ok
- Développer exportation en vCard (Corentin) - ?

**Troisième étape :**
- Développer modules supplémentaires pour l’affichage (Mathilde) - ?
- Développer modules supplémentaires pour le fonctionnement (Clara) - 
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
>> Implémenté : barre de recherche, requête associée  

Modules ajoutable en fin de développement :
> Trier les contacts par nom, prénom ou catégorie
>> Modèle implémenté, vue ok ? : critère de tri, requête associée

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
