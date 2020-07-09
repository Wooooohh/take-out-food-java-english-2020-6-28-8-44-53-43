import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        // TODO: write code here
        // 总价
        double total = 0;
        // 活动1的减免金额
        double pro1_discount = 0;
        // 活动2的减免金额
        double pro2_discount = 0;
        // 活动1减免后的价格
        double pro1_price = 0;
        // 活动2减免后的价格
        double pro2_price = 0;
        //活动策略信息
        SalesPromotion pro1,pro2;
        //所有菜品信息
        List<Item> Items;
        //活动信息
        List<SalesPromotion> SalesPromotions;
        //菜品map
        HashMap<String, Item> ItemsMap;
        Items = itemRepository.findAll();
        SalesPromotions = salesPromotionRepository.findAll();
        IteMap = new HashMap<>();
        for (Item item : Items) {
            ItemsMap.put(item.getId(), item);
        }
        pro1 = SalesPromotions.get(0);
        pro2 = SalesPromotions.get(1);

        StringBuilder ret = new StringBuilder();
        ret.append("============= Order details =============\n");
        String dishes = "";// 菜单
        for (String ele : inputs) {
            String[] item = ele.split(" x ");
            String id = item[0];// 商品id
            int amount = Integer.valueOf(item[1]);// 商品数量
            Item item1 = ItemsMap.get(id); // 商品
            double price = item1.getPrice() * amount; // 商品总价
            ret.append(String.format("%s x %d = %.0f yuan\n",item1.getName(),amount,price));
            total += price;
            if (pro2.getRelatedItems().contains(id)) {
                if(dishes.length() != 0) {
                    dishes += "，";
                }
                dishes += item1.getName();
                pro2_discount += price / 2;
            }
        }
        if(total >= 30){
            pro1_discount = 6;
        }
        ret.append("-----------------------------------\n");
        pro1_price = total - pro1_discount;
        pro2_price = total - pro2_discount;
        if(pro1_discount!=0 || pro2_discount!=0){
            ret.append("Promotion used:\n");
            if(pro1_discount >= pro2_discount){
                ret.append(String.format("满30减6 yuan，saving %.0f yuan\n",pro1_discount));
                total -= pro1_discount;
            }else{
                ret.append(String.format("Half price for certain dishes (%s)，saving %.0f yuan\n",dishes,pro2_discount));
                total -= pro2_discount;
            }
        }
        ret.append("-----------------------------------\n");
        ret.append(String.format("Total：%.0f yuan\n",total));
        ret.append("===================================");
        return ret.toString();
    }
}
