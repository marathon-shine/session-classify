package com.netease.ysf.shine.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetTypeInfo {
    static String typeMap =
            "694975\t拒收核实\n" +
            "694980\t时效内物流未更新\n" +
            "694982\t因个人急用催促\n" +
            "694989\t其他签收方式\n" +
            "695402\t脱皮、开裂\n" +
            "695403\t划痕、异味\n" +
            "695407\t商品无法启动、故障\n" +
            "696504\t超时物流未更新\n" +
            "696527\t收件人地址\n" +
            "696537\t促销期间差价\n" +
            "696538\t商品问题重新购买退差价\n" +
            "697558\t正常购买\n" +
            "697559\t大促特定时效\n" +
            "697620\t考拉承担\n" +
            "698732\t未超时售后审核\n" +
            "699511\t外包装完好，内件破损\n" +
            "706054\t化妆品过敏\n" +
            "710088\t时效内未收到催进度\n" +
            "747990\t无需回复用户\n" +
            "787122\t需考拉客服回复\n" +
            "858791\t外包装无破损\n" +
            "883036\t个人原因退货\n" +
            "920514\t保质期【有效日期】\n" +
            "920518\t配置【兼容配件、设备、适配型号】\n" +
            "920519\t验证正品【验证方法查询】\n" +
            "920528\t使用方法【入口类服用方式】\n" +
            "920529\t使用方法【安装、使用步骤（中文说明书）】\n" +
            "920532\t使用方法【商品功能】\n" +
            "966474\t预付定金\n" +
            "973944\t特殊用户退差价\n" +
            "1223546\t成分材质\n" +
            "1228523\t其他问题\n" +
            "1228704\t同款差异\n" +
            "1232355\t补货、上架咨询\n" +
            "1235155\t产品性能\n" +
            "1236157\t售后政策\n" +
            "1236158\t礼盒包装\n" +
            "1236159\t验真方式\n" +
            "1236161\t推荐搭配\n" +
            "1236162\t产地咨询\n" +
            "1236163\t物流发货\n" +
            "1236164\t税费咨询\n" +
            "1236167\t版本型号\n" +
            "1236170\t商品用法\n" +
            "1236172\t功效周期\n" +
            "1236175\t适用人群\n" +
            "1236176\t颜色尺码\n" +
            "1236178\t效期咨询\n" +
            "1236180\t其他问题\n" +
            "1236332\t商品规格\n" +
            "1236333\t价格异常、浮动\n" +
            "1245395\t更改商品取消\n" +
            "1352569\t实名认证（订购人与支付人不一致）\n" +
            "3750824\t配置配件\n" +
            "3953997\t活动时间\n" +
            "3953998\t活动方式\n" +
            "3960834\t赠品咨询\n" +
            "3969519\t优惠券使用规则\n" +
            "3977050\t退定金\n" +
            "3977051\t预付定金规则\n";

    public static String getTypeInfo(String input) throws IOException {
        String[] typeMapSplit = typeMap.split("\n");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < typeMapSplit.length; i++) {
            try {
                String line = typeMapSplit[i];
                String[] split = line.split("\t");
                map.put(split[0].trim(), split[1].trim());
            } catch (Exception e) {
                // Ignore
            }
        }
        String desc = map.get(input);
        return desc == null ? "未分类" : desc;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getTypeInfo("3977051"));
    }
}
