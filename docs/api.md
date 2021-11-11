---
hide:
- navigation
---
# Developer API

### DiceRollEvent
As of version `2.3.0` VestriaDice fires a custom `DiceRollEvent` every time a dice is rolled by a player. This event is cancellable and allows you to get the player rolling the dice, the result and the modifier. As of now these values are not modifiable but you can use them if you wish.