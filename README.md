# Smart Pantry Manager

Smart Pantry Manager is an Android application designed to help users keep track of the ingredients they already have at home and find recipes they can make using those ingredients.

The application combines pantry management with recipe recommendations. Instead of suggesting recipes that require users to buy additional ingredients, the main recommendation feature checks what is currently available in the pantry and only recommends recipes when all the required ingredients are available in sufficient quantities.

The application also helps users keep track of ingredient expiry dates so that food that needs to be used soon is easier to identify.

## Main Features

Smart Pantry Manager allows users to:

- Add ingredients to their pantry
- Edit existing ingredients
- Delete ingredients
- Search for ingredients in the pantry
- Record ingredient quantities and units
- Add optional expiry dates
- View warnings for ingredients that are close to expiring or have expired
- Sort pantry ingredients according to expiry date
- View recipes that can currently be made
- Browse the full recipe collection
- See how closely each recipe matches the current pantry
- View the ingredients and preparation method for each recipe
- Access an application settings and information screen

## Recipe Matching

The "What Can I Make?" feature uses strict recipe matching.

A recipe is only displayed on this screen when every ingredient required by the recipe is available in the pantry and the available quantities are sufficient.

For example, if a recipe requires two eggs but the pantry only contains one egg, the recipe will not appear under "What Can I Make?".

The separate "View All Recipes" screen allows users to browse the complete recipe collection. This screen shows how many of the required ingredients are currently available and provides a match percentage. This means users can still explore recipes that they cannot currently make without including incomplete recipes in the main recommendations.

The matching process also handles some common differences in ingredient names, such as singular and plural forms. Basic unit conversions between grams and kilograms, as well as millilitres and litres, are also supported when ingredient quantities are compared.

## Recipe Data

Smart Pantry Manager contains 20 recipes that are stored in the local database.

Each recipe contains:

- A recipe name
- Required ingredients
- Required quantities and units
- Preparation instructions

The recipe requirements are compared with the ingredients currently stored in the user's pantry to determine whether a recipe can be made.

## Database

Smart Pantry Manager uses SQLite with `SQLiteOpenHelper` for local data storage.

SQLite was selected because the application needs to store pantry ingredients and recipe information directly on the user's device. The main features of the application can therefore work without depending on an internet connection or an external database.

The database structure also makes it possible to separate pantry ingredients, recipes and recipe ingredient requirements while still allowing this information to be used together during recipe matching.

The pantry supports the full CRUD process:

- Create - add a new ingredient
- Read - display saved pantry ingredients
- Update - edit an existing ingredient
- Delete - remove an ingredient

The data is stored persistently, which means pantry information remains available after the application has been closed and reopened.

## Technologies Used

The main technologies and tools used to build Smart Pantry Manager are:

- Java
- Android Studio
- SQLite
- SQLiteOpenHelper
- RecyclerView
- Android XML layouts
- Git
- GitHub

## Running the Application

To run Smart Pantry Manager:

1. Clone or download the project repository.
2. Open the project in Android Studio.
3. Allow Gradle to finish syncing the project.
4. Start an Android emulator or connect a compatible Android device.
5. Select the device in Android Studio.
6. Select the `app` configuration.
7. Run the application.
8. Wait for Smart Pantry Manager to install and open on the selected device.

The application has been developed and tested using an Android emulator in Android Studio.

## Using Smart Pantry Manager

When the application opens, the Pantry screen displays the ingredients that are currently saved.

The "+ Add Ingredient" button can be used to add a new pantry item. The ingredient name, quantity and unit are required, while adding an expiry date is optional.

Existing ingredients can be edited when their details or quantities change. Ingredients that are no longer available can also be deleted from the pantry.

The search feature can be used to quickly find an ingredient when there are several items saved in the pantry.

Expiry information is displayed for ingredients with expiry dates. Ingredients that have expired or are approaching their expiry dates are highlighted so that they can be identified more easily.

The "What Can I Make?" option checks the current pantry and displays only recipes for which all required ingredients are available in sufficient quantities.

The "View All Recipes" option displays the complete recipe collection. Recipes are shown with information about how closely they match the ingredients currently available in the pantry.

Selecting "View Recipe" opens the recipe details where the required ingredients and preparation instructions can be viewed.

## Project Purpose

The purpose of Smart Pantry Manager is to provide a simple pantry management system that helps users keep track of the ingredients they already have and make better use of them.

The application combines pantry management with recipe recommendations. Instead of suggesting recipes that require users to buy additional ingredients, Smart Pantry Manager checks the ingredients and quantities currently available in the pantry and recommends recipes that can be made using what is already there.

The system is also designed to help reduce unnecessary food waste by allowing users to record expiry dates and identify ingredients that should be used soon. Users can manage their pantry, keep track of quantities, search for ingredients and explore recipes based on their available food.

Overall, the aim is to make pantry management more organised while helping users decide what they can cook with the ingredients they already have.