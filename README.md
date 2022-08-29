# FeedMe (en cours de développement)
Application de gestion de commandes de paniers de fruits et légumes. 

### Fonctionnalités : 
- Création de clients, commands, produits
- Dashboard 
- Gestion des statuts de réalisation des commandes
- Gestion de la livraison des commandes (Google Maps API ?)

## Quelques écrans 

<p float="center">
  <img src="https://user-images.githubusercontent.com/42738178/185603705-20255466-34e8-47bf-a2c5-49215cf44298.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/185606971-7e7d4db9-e30d-4e1a-ba89-3b7fd8e5b6aa.png" width="240" /> 
  <img src="https://user-images.githubusercontent.com/42738178/185606461-a7a95db2-def3-4503-805c-0cceacd4cc48.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/185606509-0be19bdf-fb17-4dea-84c3-7538152500eb.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/185606485-2e80922b-a776-4007-9e31-51c48275ba31.png" width="240" />
</p>

## Démo 

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

*L'architecture et la conception - réalisés en [plantUml](https://plantuml.com/fr/) - sont séparées dans un module "Documentation" et seront mis à jour à chaque changement de modèle.*

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
