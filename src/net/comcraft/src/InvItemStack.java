package net.comcraft.src;

// ModLoader start
import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

// ModLoader end
public final class InvItemStack extends JsObject { // ModLoader

    public int stackSize;
    public int itemID;
    private int itemDamage;
    // ModLoader start
    private static final int ID_GET_ITEM = 100;
    private static final int ID_GET_ITEM_DAMAGE = 101;
    private static final int ID_SET_ITEM_DAMAGE = 102;
    public static final int ID_CONSTRUCT = 103;
    private static final int ID_STACK_SIZE = 104;
    private static final int ID_ITEM_ID = 106;
    public static final JsObject ITEMSTACK_PROTOTYPE = new JsObject(OBJECT_PROTOTYPE).addNative("getItem", ID_GET_ITEM, 0)
            .addNative("getItemDamage", ID_GET_ITEM_DAMAGE, 0).addNative("setItemDamage", ID_SET_ITEM_DAMAGE, 1).addNative("stackSize", ID_STACK_SIZE, -1)
            .addNative("itemID", ID_ITEM_ID, -1);

    // ModLoader end

    public InvItemStack(int itemId, int stackSize) {
        this();
        this.itemID = itemId;
        this.stackSize = stackSize;
    }

    public InvItemStack(int itemID) {
        this(itemID, 1);
    }

    public InvItemStack(Block block) {
        this(block.blockID, 1);
    }

    public InvItemStack() {
        super(ITEMSTACK_PROTOTYPE);
    }

    public InvItem getItem() {
        if (itemID == 0 || stackSize == 0) {
            return null;
        }
        InvItem item = InvItem.itemsList[itemID];
        if (item == null) {
            throw new ComcraftException("Unknown item id " + itemID, null);
        }
        return item;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public void setItemDamage(int value) {
        itemDamage = value;
    }

    public boolean useItem(EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        boolean flag = getItem().onItemUse(this, entityPlayer, world, x, y, z, side);
        return flag;
    }

    public boolean equals(Object object) {
        if (object instanceof InvItemStack) {
            InvItemStack itemStack = (InvItemStack) object;

            return itemStack.itemID == itemID;
        } else {
            return super.equals(object);
        }
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.itemID;
        return hash;
    }

    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_CONSTRUCT:
            itemID = stack.getInt(sp + 2);
            if (parCount > 1) {
                stackSize = stack.getInt(sp + 3);
            } else {
                stackSize = 1;
            }
            break;
        case ID_GET_ITEM:
            stack.setObject(sp, getItem());
            break;
        case ID_GET_ITEM_DAMAGE:
            stack.setInt(sp, getItemDamage());
            break;
        case ID_SET_ITEM_DAMAGE:
            setItemDamage(stack.getInt(sp + 2));
            break;
        case ID_STACK_SIZE:
            stack.setInt(sp, stackSize);
            break;
        case ID_STACK_SIZE + 1:
            stackSize = stack.getInt(sp);
            break;
        case ID_ITEM_ID:
            stack.setInt(sp, itemID);
            break;
        case ID_ITEM_ID + 1:
            itemID = stack.getInt(sp);
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
}
