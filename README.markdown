# Coupon Codes for Minecraft  
[Commands](#commands)
[Installing](#install)
## Basic Coupon commands:<a name='commands'>
Console: coupon    
Talk: /c  
[] = required  
() = optional  
### Redeeming:
	/c redeem [coupon code]  
[coupon code] = code to redeem.  this is the command most players care about.    
### Adding:
#### Items  
	/c add item [coupon code] [item code:amount(:damage(:enchantment))] (active) (total uses) (expiration)
[coupon code] = string used to redeem this coupon.  no spaces.  
[item code] = id of a block or item from [Minecraft Wiki](http://www.minecraftwiki.net/wiki/Data_values)  
[amount] = how many of the item to give.  
(damage) = allows you to color your wool.  go figure.  
(enchantment) = id of enchantment to apply from [Bukkit::Enchantments](http://jd.bukkit.org/doxygen/dd/d17/classorg_1_1bukkit_1_1enchantments_1_1Enchantment.html)   
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +1:hour  
#### XP
	/c add xp [coupon code] [xp] (active) (total uses) (expiration)
[coupon code] = string used to redeem this coupon.  no spaces.  
[xp] = amount of xp to reward.  
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +60:min  
#### Rank
	/c add rank [coupon code] [rank] (active) (total uses) (expiration)
[coupon code] = string used to redeem this coupon.  no spaces.  
[xp] = amount of xp to reward.  
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +30:seconds  
#### Economy
	/c add econ [coupon code] [money] (active) (total uses) (expiration)
[coupon code] = string used to redeem this coupon.  no spaces.  
[money] = amount of money to reward.  
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +90:days  
#### Warp
	/c add warp [coupon code] [x] [y] [z] (active) (total uses) (expire)
[coupon code] = string used to redeem this coupon.  no spaces.  
[x/y/z] = coordinates to teleport player to.  if any don't parse as numbers, they will be replaced with the players current respective coordinate. ex: 'X 256 X' to drop someone from the sky.   
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +90:days  
#### Bad
	/c add bad [coupon code] [effect] (active) (total uses) (expire)
[coupon code] = string used to redeem this coupon.  no spaces.  
[effect]
-	burn = set player on fire
-	explode = player explodes like TNT
-	chicken = spawns player some chickens
-	lightning = hits the player with lightning
-	poison = poisons the player
-	drop = teleport the player high up into the sky
(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.  
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +90:days  
#### Multi
	/c add multi [coupon code(:count)] [subcoupon1(:subcoupon2...)] (total uses) (expire)
[coupon code] = string used to redeem this coupon.  no spaces.  
(count) = use [coupon code] as a prefix, and generate (count) unique codes.  
[subcoupon1(:subcoupon2...)] = semicolon seperated list of coupons that you want this "multi" coupon to apply.   
(total uses) = default 1 use.  
(expiration) = period coupon is good for.  example: +3600:sec    
### Deleting:
	/c remove [coupon code/all]
[coupon code] = code of the coupon to remove.  
[all] = delete everything.  BOOM.  
### Listing:
	/c list (all/inactive/prefix)
(all) = show all coupons, instead of just active ones  
(inactive) = show only inactive coupons  
(prefix) = show all coupons begining with the (prefix)  
### Information:
	/c info (coupon code)
(coupon code) = show information the specific coupon, instead of general info  
## Installation<a name='install'>
Under development