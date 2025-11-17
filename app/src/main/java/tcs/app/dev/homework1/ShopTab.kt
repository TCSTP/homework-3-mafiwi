package tcs.app.dev.homework1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Discount

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tcs.app.dev.R
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Item
import tcs.app.dev.homework1.data.Shop
import tcs.app.dev.homework1.data.Discount
import tcs.app.dev.homework1.data.Euro
import tcs.app.dev.homework1.data.MockData
import tcs.app.dev.homework1.data.minus
import tcs.app.dev.homework1.data.plus
import tcs.app.dev.homework1.data.update

/* 1) **Shop item tab**
*    - Show all items offered by the shop, each row displaying:
*      - item image + name,
*      - item price,
*      - an *Add to cart* button.
*    - Tapping *Add to cart* increases the count of that item in the cart by 1. */

@Composable
fun ItemRow(
 item: Item? = null,
 discount: Discount? = null,
 trailingContent: @Composable RowScope.() -> Unit = {}
){
 Card(
  modifier = Modifier
   .fillMaxWidth()
   .padding(horizontal = 8.dp, vertical = 4.dp)
 ) {
  Row(
   modifier = Modifier
    .padding(8.dp)
    .fillMaxWidth(),
   verticalAlignment = Alignment.CenterVertically,
   horizontalArrangement = Arrangement.SpaceBetween
  ) {
   Row(verticalAlignment = Alignment.CenterVertically) {
    when {
     item != null -> {
      Image(
       painter = painterResource(id = MockData.getImage(item)),
       contentDescription = stringResource(id = MockData.getName(item)),
       modifier = Modifier
        .size(64.dp)
        .padding(end = 8.dp)
      )
      Text(stringResource(id = MockData.getName(item)))
     }

     discount != null -> {
      val discountText = when (discount) {
       is Discount.Fixed -> stringResource(R.string.amount_off, discount.amount.toString())
       is Discount.Percentage -> stringResource(R.string.percentage_off, discount.value.toString())
       is Discount.Bundle -> stringResource(
        R.string.pay_n_items_and_get,
        discount.amountItemsPay.toString(),
        stringResource(MockData.getName(discount.item)),
        discount.amountItemsGet.toString()
       )

       else -> "Unknown discount"
      }

      Icon(
       imageVector = Icons.Outlined.Discount,
       contentDescription = "Discount icon",
       modifier = Modifier.size(48.dp)
      )
      Spacer(modifier = Modifier.size(8.dp))
      Text(discountText)
     }
    }
   }
   Row(verticalAlignment = Alignment.CenterVertically, content = trailingContent)
  }
 }}
@Composable
fun ShopTab(
 shop: Shop,
 onAddToCart: (Item) -> Unit
) {
 LazyColumn(
  modifier = Modifier.fillMaxSize(),
  verticalArrangement = Arrangement.spacedBy(8.dp)
 ) {
  items(shop.items.toList()) { item ->
   ItemRow(
    item = item,
    trailingContent = {
     Column(horizontalAlignment = Alignment.End) {
      Text(shop.prices[item].toString())
      Spacer(modifier = Modifier.size(4.dp))
      Button(onClick = { onAddToCart(item) }) {
       Text(stringResource(R.string.description_add_to_cart))
      }
     }
    }
   )
  }
 }
}

@Composable
fun CartTab(
 cart: Cart,
 onIncrease: (Item) -> Unit,
 onDecrease: (Item) -> Unit,
 onRemoveDiscount: (Discount) -> Unit
) {
 if (cart.items.isEmpty() && cart.discounts.isEmpty()) {
  Box(
   modifier = Modifier.fillMaxSize(),
   contentAlignment = Alignment.Center
  ) {
  }
  return
 }

 LazyColumn(
  modifier = Modifier.fillMaxSize(),
  verticalArrangement = Arrangement.spacedBy(8.dp),
  contentPadding = PaddingValues(8.dp)
 ) {
  items(cart.items.entries.toList()) { (item, amount) ->
   ItemRow(
    item = item,
    trailingContent = {
     Row(verticalAlignment = Alignment.CenterVertically) {  Text("${Euro((cart.shop.prices[item]!!.cents * amount))}")
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Button(onClick = { onIncrease(item) }) { Text(stringResource(R.string.description_increase_amount)) }
       Text(amount.toString(), modifier = Modifier.padding(horizontal = 8.dp))
       Button(onClick = { onDecrease(item) }) { Text(stringResource(R.string.description_decrease_amount)) }
      }
    }}
   )
  }
  items(cart.discounts) { discount ->
   ItemRow(
    discount = discount,
    trailingContent = {
     Button(onClick = { onRemoveDiscount(discount) }) { Text(stringResource(R.string.description_remove_from_cart)) }
    }
   )
  }
 }
 }


@Composable
fun DiscountTab(
 shop: Shop,
 cart: Cart,
 onAddDiscount: (Discount) -> Unit
) {
 LazyColumn(
  modifier = Modifier.fillMaxSize(),
  verticalArrangement = Arrangement.spacedBy(8.dp),
  contentPadding = PaddingValues(8.dp)
 ) {
  items(MockData.ExampleDiscounts) { discount ->
   val alreadyInCart = cart.discounts.contains(discount)

   ItemRow(
    discount = discount,
    trailingContent = {
     Button(
      onClick = { onAddDiscount(discount) },
      enabled = !alreadyInCart
     ) {
      Text(stringResource(R.string.description_add_to_cart))
     }
    }
   )
  }
 }
}



