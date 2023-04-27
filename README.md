# OSS
Online Shopping Service

## Todo List

must have features

- [x] The quantity of each product in a shopping cart can be greater than one.
- [x] When create new product, user can decide whether the new product can be used as gift or not. If the product can be used as gift, user add this product into `ShoppingCart` as an `CartItem` and set/get greeting message to it.
- [x] Data input
- [x] Update `ShoppingCart`
  - [x] `boolean addItem(Product product, int quantity)`
  - [x] `boolean addItem(UUID itemId, Product product, int quantity)`
  - [x] `boolean removeItem(UUID itemId, Product product, int quantity)`
- [x] Coupon
- [x] Tax
- [x] Print purchase receipts to console or stored in a text file
- [x] Sort a list of all ShoppingCart by total weight (in increasing order)
- [x] Update UI
- [x] Unit Test

documentation
- [] Demo video (max 7 minutes, normal play speed)
- [] PDF report (max 8 pages)
  - [] OOP design explanation
  - [x] UML diagram
  - [] Describe automatic/manual tests that are used to ensure quality of the system.


## Coupon

The system also supports coupon usage. Each coupon is **tied to a specific product** and has a unique `String` value. That means a coupon for product X can only be used on a shopping cart that contains at **least one** item of product X. Once applying a coupon on a shopping cart, that coupon will apply to every corresponding product item.

A coupon can be a `price coupon` or a `percent coupon`. A price coupon contains a `double` value which is the amount reduced in the product price when applying this coupon. For example, if a product has a price of 12.3, and a price coupon has a value of 1.2, then the price of the product when applying the coupon is 11.1. A percent coupon contains an `integer` from 1 to 99 which is the percent reduced in the product price when applying this coupon. For example, if a product has a price of 20, a percent coupon has a value of 10, then the price of the product when applying this coupon is 18 (10% off).

You must decide the class/method that allows you to apply a coupon to a shopping cart. A shopping cart contains **at most one** coupon. So, if you apply a new coupon, the existing coupon (if any) will be removed.

## Total Amount (tax & shipping fee)

Besides product prices, customers must pay taxes associated with them. **Each product has a tax type**, which can be either: `tax-free`, `normal tax`, or `luxury tax`. You can set the amount of those tax types to `0%`, `10%`, and `20%` respectively. But make sure that we can update those numbers later easily without changing much code. The tax amount is calculated based on the product price without applying any coupon. For example, if a product has a price of 20 and has a luxury tax type, the tax amount of that product is 4.

The final amount customers must pay for their purchases also includes a shipping fee component. The shipping fee of each cart depends on the total weight of all physical product items in the cart. **Note that the shipping fee is not taxable**. The following formula is used to calculate the shipping fee of a cart:

```
shipping fee = (total weight of all physical products) * (base fee)
```

Let's use a fixed value of `0.1` for the base fee. However, you must ensure that it's easy to update this base fee when required.

You need to add a method to calculate and return the total amount a customer has to pay for a shopping cart. This total amount includes product price, tax, and shipping fee.

## Data Input

To support the testing of your program, you must store around 30-50 products of all different types (e.g., digital, physical, gift, tax, coupon, etc.) in a text file `products.txt` (you decide the format of the file, however, it must be a text file and can be readable by a normal text editor program like notepad). Then, when your program starts, it must use the Stream API to read this file and create all necessary products. In addition, you must create another file `carts.txt` to store information about 5-10 carts. The information stored for each cart includes its product items (and quantity), the greeting messages for gift product items (if there are gift product items in the specified cart), the coupon applied to the cart (if any), etc. Similar to products.txt, you must use `Stream API to` read carts.txt and create the required shopping carts.

## User Interaction

UI requirement: this is a console application.

First, your program reads products.txt, carts.txt and creates all product/shopping cart objects (according to the above description).

Then, display a menu for users to:

- View/add/edit products
- Add/remove product items to/from shopping carts
- Update/view messages for gift product items
- Apply/remove coupons
- Select a cart and view its details including total price, tax, and shipping fee
- Sorting carts
- Print purchase receipts to the screen or a text file (users specify a name for the file)
