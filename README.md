## ember
393 Software Engineering Project


**Work Plan**
1. Decide what goes into the search, and use that to design SearchRestriction class
2. Write MovieSet DAL based on SearchRestriction design
2. Learn Android environment and design GUI
3. Logic backside of the GUI (user input into SearchRestriction and the returned MovieSet back into the GUI, through the suggestion logic)
3. Suggestion logic
4. Functional testing of GUI
4. Functional testing of suggestion logic
5. Functional testing off entire app

**Roles**
- write MovieSet data access layer, and Movie class
- write suggestion algorithm
- write GUI control logic
- learn Android environment and design actual GUI
- write SearchRestriction and decide how to turn user input into a SearchRestriction

Classes
App - main class
//whatever classes are needed to run the GUI logic

Movie - the information about an individual movie
MovieSet - collection of movies

SearchRestriction - accesses GUI and pulls the set of Restrictions that the user has input
//whatever classes are needed to create a SearchRestriction, or if it makes more sense for it to be a set of multiple SearchRestrictions
