# Rating Manager
This is my "Data Structures" course project.

The objective was to find a way to store and retrieve objects (ratings) as efficiently as possible.

"Ratings" consisted of 3 attributes:
* userID for the user who made the rating.
* itemID for the item that was rated.
* rating for the rating value (1-5).

The objects were retrieved based on one or more of the attributes (e.g. the top ten highest rated items). As such, the objects would have to be stored in a manner that was efficient to access.

The end result consisted of two BSTs (binary search trees), the nodes of which consisted of BSTs, the nodes of which consisted of Rating objects.

The higher levelled BST would use one attribute (userID or itemID) as the key to its nodes (BST), while the lower levelled BST would use the other attribute as the key to its nodes (Rating). The end result was two BSTs, one of which sorted its users, and under them it sorted every item they'd ever rated. The other BST did the same but in reverse.
