package com.acme.widgets

import com.acme.widgets.inventory.InventoryProduct
import com.acme.widgets.orders.OrderItem
import com.acme.widgets.orders.PurchaseOrder
import com.acme.widgets.products.Product
import grails.transaction.Transactional

@Transactional
class PurchaseOrderService {

    InventoryService inventoryService

    /**
     * Creates a new order
     * NOTE: If any of the skus do not exist the operation fails
     * @param skuCountMap
     */
    Map<String, String> createNewOrder( Map<String, Integer> skuCountMap) {

        Map<String, String> errors = [:]

        if (verifyInventoryForCreate(skuCountMap)) {

            PurchaseOrder purchaseOrder
            purchaseOrder = new PurchaseOrder().save()
            OrderItem orderItem
            skuCountMap.keySet().each { String sku ->

                orderItem = new OrderItem(sku: sku, total: skuCountMap[sku], purchaseOrder: purchaseOrder)
                purchaseOrder.addToItems(orderItem)
            }

            //TODO:  Add transaction to update inventor to remove quantity of products
            //       based on order

            purchaseOrder.save(flush: true)
        } else {
            //TODO: Could enhance message to show which items are lacking
            errors["inventory"] = "po.error.create.insufficient.inventory"

        }

        return errors

    }

    /**
     *
     * @param PurchaseOrder
     * @param skuCountMap
     */
    def updatePurchaseOrder(purchaseOrder, Map<String, Integer> skuCountMap) {

        //Lookup purchase order
        Map errors = [:]


        if (purchaseOrder) {

            InventoryProduct inventoryProduct
            //For each sku in the map, either create or update the inventory
            Integer productCount = 0
            Integer itemRequestCount = 0
            skuCountMap.keySet().each { String sku ->

                itemRequestCount = skuCountMap[sku]
                verifyAndupdatePurchaseOrderForSku(purchaseOrder, itemRequestCount, sku, errors)
            }
        } else {
            errors[purchaseOrder.id] = 'po.no.resource'
        }

        println errors
        return errors

    }

    /**
     *
     * @param purchaseOrder
     * @param requestAmount
     * @param sku
     * @param errors
     * @return
     */
    private
    static verifyAndupdatePurchaseOrderForSku(PurchaseOrder purchaseOrder, Integer requestAmount, String sku, Map errors) {

        InventoryProduct inventoryProduct = InventoryProduct.findByProduct(Product.findBySku(sku))

        OrderItem orderItem = purchaseOrder.items.find { it.sku == sku }
        if (orderItem) {

            //Only update the order if the difference
            if (inventoryProduct.count >= (requestAmount - orderItem.total)) {
                //Request amount - current Item total >= inventory amount
                orderItem.total = requestAmount
            } else {
                errors[sku] = 'po.error.not.enough.inventory'
            }

            // Only commit changes when there are no errors
            if (errors.isEmpty()) {
                purchaseOrder.save(flush: true)
            }

        } else {

            //FIXME: Skipping Inventory Check
            purchaseOrder.addToItems(new OrderItem(sku: sku, total: requestAmount))
        }
    }

    /**
     * Checks that there is enough inventory for all the counts in the skuCount map
     * @param skuCountMap
     * @return
     */
    boolean verifyInventoryForCreate(Map<String, Integer> skuCountMap) {

        //FIXME: Implement at later day
        boolean hasInventory = true


        return hasInventory
    }

}
