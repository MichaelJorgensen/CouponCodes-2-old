# Coupon Codes for Minecraft  
## Basic command:
Console: coupon  
Talk: /c  
## Adding coupons:
### Items  
	/c add item [coupon code] [item code:amount(:damage(:enchantment))] (active) (total uses) (expiration)
	[coupon code] = string used to redeem this coupon.  no spaces.
	[item code] = id of a block or item from [Minecraft Wiki](http://www.minecraftwiki.net/wiki/Data_values)
	[amount] = how many of the item to give.
	[damage] = allows you to color your wool.  go figure.
	[enchantment] = id of enchantment to apply from [Bukkit::Enchantments](http://jd.bukkit.org/doxygen/dd/d17/classorg_1_1bukkit_1_1enchantments_1_1Enchantment.html) 
	(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.
	(total uses) = default 1 use.
	(expiration) = period coupon is good for.  example: +1:hour
### XP
	/c add xp [coupon code] [xp] (active) (total uses) (expiration)
	[coupon code] = string used to redeem this coupon.  no spaces.
	[xp] = amount of xp to reward.
	(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.
	(total uses) = default 1 use.
	(expiration) = period coupon is good for.  example: +60:min
### Rank
	/c add rank [coupon code] [rank] (active) (total uses) (expiration)
	[coupon code] = string used to redeem this coupon.  no spaces.
	[xp] = amount of xp to reward.
	(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.
	(total uses) = default 1 use.
	(expiration) = period coupon is good for.  example: +30:seconds
### Economy
	/c add econ [coupon code] [money] (active) (total uses) (expiration)
	[coupon code] = string used to redeem this coupon.  no spaces.
	[money] = amount of money to reward.
	(active) = default 1 for active, 0 for inactive.  inactive coupons can not be redeemed, except by a "multi" coupon.
	(total uses) = default 1 use.
	(expiration) = period coupon is good for.  example: +90:days
### Multi
	/c add multi [coupon code(:count)] [subcoupon1(:subcoupon2...)] (total uses) (expire)
	[coupon code] = string used to redeem this coupon.  no spaces.
	(count) = use [coupon code] as a prefix, and generate (count) unique codes.
	[subcoupon1(:subcoupon2...)] = semicolon seperated list of coupons that you want this "multi" coupon to apply. 
	(total uses) = default 1 use.
	(expiration) = period coupon is good for.  example: +90:days
