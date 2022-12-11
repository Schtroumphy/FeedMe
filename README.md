# FeedMe (en cours de développement)
Application de gestion de commandes de paniers de fruits et légumes. 

### Fonctionnalités : 
- Création de clients, commands, produits
- Dashboard 
- Gestion des statuts de réalisation des commandes
- Gestion de la livraison des commandes (Google Maps API ?)

## Quelques écrans 

<p float="center">
  <img src="https://user-images.githubusercontent.com/42738178/206867415-b3328c3b-e37c-45ae-8a7b-96d74f780d01.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/206867468-0260bd53-16eb-4d4b-860b-ec8b5c1d2d9a.png" width="240" /> 
  <img src="https://user-images.githubusercontent.com/42738178/206867495-6d213b99-024e-40cb-981b-b25e9e384ca7.png" width="240" />

  <img src="https://user-images.githubusercontent.com/42738178/206867681-e5b143e9-e14a-4bac-ab7f-3591f10fe502.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/206867686-4d5b0d56-37ad-4f89-b164-2ee78d35ee83.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/206867690-d5916e3b-12a9-40a5-9ffa-1da59b149821.png" width="240" />
  
  <img src="https://user-images.githubusercontent.com/42738178/206867670-6fb71dd3-b50c-4048-9693-661bddc523f6.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/206867675-77b4cd45-b091-451d-bdd5-876540fa4626.png" width="240" />
  
  <img src="https://user-images.githubusercontent.com/42738178/206867659-04c79a9e-3ad5-47f6-b76b-42e8f5252daa.png" width="240" />
  <img src="https://user-images.githubusercontent.com/42738178/206867666-b4598849-b81d-4bde-b309-255b2b44ea60.png" width="240" />

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
