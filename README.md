# FeedMe (en cours de développement)
Application de gestion de commandes de paniers de fruits et légumes. 

### Fonctionnalités : 
- Création de clients, commands, produits
- Dashboard 
- Gestion des statuts de réalisation des commandes
- Gestion de la livraison des commandes (Google Maps API ?)

## Quelques écrans 

<p float="center">
  <img src="https://user-images.githubusercontent.com/42738178/206867415-b3328c3b-e37c-45ae-8a7b-96d74f780d01.png" width="240" alt=""/>
  <img src="https://user-images.githubusercontent.com/42738178/206867468-0260bd53-16eb-4d4b-860b-ec8b5c1d2d9a.png" width="240" alt=""/> 
  <img src="https://user-images.githubusercontent.com/42738178/206867495-6d213b99-024e-40cb-981b-b25e9e384ca7.png" width="240" alt=""/>

  <img src="https://user-images.githubusercontent.com/42738178/206867681-e5b143e9-e14a-4bac-ab7f-3591f10fe502.png" width="240" alt=""/>
  <img src="https://user-images.githubusercontent.com/42738178/206867686-4d5b0d56-37ad-4f89-b164-2ee78d35ee83.png" width="240" alt=""/>
  <img src="https://user-images.githubusercontent.com/42738178/206867690-d5916e3b-12a9-40a5-9ffa-1da59b149821.png" width="240" alt=""/>
  
  <img src="https://user-images.githubusercontent.com/42738178/206867670-6fb71dd3-b50c-4048-9693-661bddc523f6.png" width="240" alt=""/>
  <img src="https://user-images.githubusercontent.com/42738178/206867675-77b4cd45-b091-451d-bdd5-876540fa4626.png" width="240" alt=""/>
  
  <img src="https://user-images.githubusercontent.com/42738178/206867659-04c79a9e-3ad5-47f6-b76b-42e8f5252daa.png" width="240" alt=""/>
  <img src="https://user-images.githubusercontent.com/42738178/206867666-b4598849-b81d-4bde-b309-255b2b44ea60.png" width="240" alt=""/>

</p>

## Démo (not updated) 

https://user-images.githubusercontent.com/42738178/185626974-43c930cb-5747-4e9a-ba30-ff1862e6da93.mp4

## Architecture
L'architecture choisie est une sorte d'architecture modulaire par feature. Au lieu d'avoir des modules au sens premier sur Android Studio, j'ai fait le choix d'avoir des packages afin de réduire la compléxité. 
- **Core** : Contient les éléments communs aux différents modules [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android) pour l'injection de dépension, les fonctions d'extensions, la configuration de la base de données [Room](https://developer.android.com/training/data-storage/room) ...)
- **Features** : Contient l'ensemble des features regroupés eux-mêmes en feature
  Chaque "feature" comprend les couches :
    - **Presentation** : Compose Screen, View Model, UiState (sealed class)
    - **Domain** : Use cases, repository abstraction, models
    - **Data**
      - local : entities, mappers, repository implementation
      - remote

![image](https://user-images.githubusercontent.com/42738178/187178141-fceb9dc1-965c-4f2e-bebd-472ba5ad29a8.png)

Ce choix me permettra par la suite de pouvoir réutiliser certain "module feature dans d'autres applications et de tester les features individuellement.

## Conception 

![image](https://user-images.githubusercontent.com/42738178/187171106-a44efd37-7501-41f0-af77-513d9dc047fe.png)

*L'architecture et la conception - réalisées en [plantUml](https://plantuml.com/fr/) - sont séparées dans un module "Documentation" et seront mises à jour à chaque changement de modèle.*

### Stack technique 

- MVVM : https://medium.com/androidmood/comprendre-larchitecture-mvvm-sur-android-aa285e4fe9dd
- Dagger-Hilt : Injection de dépendances (https://developer.android.com/training/dependency-injection/hilt-android)
- Room : Base de donnée relationnelle (https://developer.android.com/training/data-storage/room)
- Jetpack Compose : https://developer.android.com/jetpack/compose
- PlantUml : https://plantuml.com/fr/

## Branches 

- main : branche principale
- master : branche de release
- v1 : Version comprenant les designs et la mise en place de l'architecture 

## Fonctionnalités MVP (LOADING)

- [x] Pouvoir ajouter des produits sans image
  - Comme pas d'image, revoir l'affichage lors du détail de la commande
- [x] Créer des commandes
- [x] Changer le statut d'une commande (à faire, en cours, en livraison, livrée, payée)
- [x] Ajouter des clients (juste avec un nom)
- [ ] Pouvoir annuler une commande (swipe vers la gauche - annulation) - Nouveau statut 'cancel'

## Définition V2 (NOT STARTED)

- [ ] Pouvoir mettre à jour une commande
- [ ] Ajouter des produits avec images (depuis le web)
- [ ] Revoir la recherche d'adresse de livraison
- [ ] Gérer les bugs à la suppression d'un client (crash)
- [ ] Revoir taille des boutons
- [ ] Pouvoir afficher la map à tout moment (même avant que la commande soit finalisée)
- [ ] Définir type de paiement et s'il est complet ou non (booléen)
- [ ] Migrer vers Koin
- [ ] Ajout de filtre sur la liste de commandes (statut, date, client, avec ou sans panier ...)
- [ ] Pouvoir supprimer une commande (swipe vers la droite)
- [ ] Pouvoir modifier un panier ?
- [ ] Pouvoir visualiser le contenu d'un panier


Technique : 
- [ ] Tests : Coverage 10% (avec définition des fichiers à ignorer)
- [ ] Passer à gitmoji pour les commits
- [ ] Migrer vers la navigation type-safe de Compose (2.8.0)
  - https://developer.android.com/guide/navigation/design/type-safety?hl=fr
  - https://developer.android.com/jetpack/androidx/releases/navigation?hl=fr
- [ ] Séparer la partie Navigation de la MainActivity

## Backlog 

### V3 ?
- [ ] Ajouter des produits avec des photos depuis le mobile
- [ ] [Paramètres] Pouvoir ajouter une adresse de départ dans un onglet 'Paramètres'
- [ ] [Parcours] Avoir un itinéraire entre l'adresse de départ et l'adresse de livraison sur la map de détail de la commande
- [ ] Pouvoir envoyer un message depuis le détail de la commande à l'utilisateur
- [ ] [Dashboard] Dynamiser entrées du dashboard avec de vraies données
- [ ] Formulaire à la première connexion ou si informations manquantes de départ (nom, adresse de départ(facultative), couleurs par statut?).
- [ ] [UI] Ajouter une icône indiquant si l'adresse est renseignée ou non pour une commande

- [ ] [Paramètres] Pouvoir changer la couleur des statuts de commandes ?
- [ ] [Parcours] Proposer un parcours par date de livraison
- [ ] [Parcours] Parcours le plus court entre plusieurs commandes (sélectionner plusieurs commandes puis donner des actions comme le parcours)
- [ ] Personnaliser à la volée le message envoyé au client (Prix, statut, informations complémentaires, contenu...) ?
- [ ] Pouvoir modifier les prix par défaut d'une commande dans l'onglet 'Paramètres'
- [ ] [Dashboard] Redirections au clic sur des items du dahsboard (vue commandes avec filtres,...)
- [ ] [Thème] Ajout d'un thème light & dark

Technique :
- [ ] Tests : Coverage 30% (avec définition des fichiers à ignorer)