package com.kasiengao.ksgframe.java.staggered;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: StaggeredGridModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:22
 * @Description: 瀑布流 数据
 */
public class StaggeredGridModel {

    /**
     * 请求 （假数据）
     */
    public List<StaggeredGridBean> requestGridBeans() {
        // 集合对象
        List<StaggeredGridBean> gridBeans = new ArrayList<>();

        StaggeredGridBean bean1 = new StaggeredGridBean();
        bean1.mPicture = "https://bkimg.cdn.bcebos.com/pic/203fb80e7bec54e7e3058fccb2389b504ec26a73?x-bce-process=image/watermark,g_7,image_d2F0ZXIvYmFpa2U5Mg==,xp_5,yp_5";
        bean1.mName = "江户川柯南";
        bean1.mContent = "故事里的男主角，“名侦探柯南”的灵魂人物，原名工藤新一";
        bean1.mDetailContent = "他是故事里的男主角，“名侦探柯南”的灵魂人物。原名工藤新一，帝丹高中二年极学生，与小兰是青梅竹马。继承了父亲的超强推理能力，帮助警方破获了不少案子，被誉为“日本警察的救世主”。他喜欢看推理小说，最崇拜福尔摩斯，踢足球的水平一流，擅长凌空抽射。被卷入黑衣组织的交易被灌下毒药APTX4869，幸运的是并没有死，只是退化成了小孩，目前年龄定为7岁。改名为江户川柯南。智力没有衰退，推理能力丝毫未减。\n" +
                "\n" +
                "为了找出黑衣人的下落，寄住在家开侦探事务所的小兰家，一方面暗地协助小五郎破案。而体力上的不足靠阿笠博士的发明来弥补。变小后就读于帝丹小学1年级B班，认识了吉田步美、小岛元太、圆谷光彦三人，以及后来加入的灰原哀，成立了少年侦探团，是其中最重要的“首脑”。不管是变小前后，唱歌五音不全还是没有办法变更的事实……唱歌走音非常夸张。";
        bean1.mWidth = 490;
        bean1.mHeight = 697;
        gridBeans.add(bean1);

        StaggeredGridBean bean2 = new StaggeredGridBean();
        bean2.mPicture = "http://b-ssl.duitang.com/uploads/item/201901/22/20190122005154_KuvtH.jpeg";
        bean2.mName = "工藤新一";
        bean2.mContent = "帝丹高中二年级2班学生、高中生侦探";
        bean2.mDetailContent = "漫画《名侦探柯南》及衍生作品的主人公和《魔术快斗》中的客串角色。17岁 [1-2]  ，高中生侦探，就读于帝丹高中二年级B班，人称“平成年代的福尔摩斯”“日本警察的救世主”。\n" +
                "在和青梅竹马毛利兰一同去游乐园玩时，目睹黑衣组织成员秘密交易时而被偷袭，并被灌下名为“APTX4869”的毒药，虽然幸免于死，但身体就此缩小成7岁小学生的模样。之后寻求阿笠博士的帮助，在被小兰询问自己名字时，化名为江户川柯南。在阿笠博士的提议下，寄住于小兰的父亲、私家侦探毛利小五郎家中，秘密调查黑衣组织";
        bean2.mWidth = 1600;
        bean2.mHeight = 900;
        gridBeans.add(bean2);

        StaggeredGridBean bean3 = new StaggeredGridBean();
        bean3.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589881905450&di=f1aac7c4aa845dab974c4424d27bbff2&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9fo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fcaef76094b36acaf547a5f1f7cd98d1000e99cbf.jpg";
        bean3.mName = "毛利兰";
        bean3.mContent = "毛利兰是故事里的女主角，帝丹高中2年级生，和新一是同班同学";
        bean3.mDetailContent = "毛利兰是故事里的女主角，帝丹高中2年级生，和新一是同班同学，血型和新一相同，对他有着超越一般朋友的感情但又不愿承认。好友为园子及和叶。她是一个坚强又温柔的女孩，擅长料理家事。因崇拜空手道冠军前田聪，便开始学习空手道且对空手道有着浓厚的兴趣，功夫也不浅，为空手道社的主将，还曾经获得东京空手道大会的冠军。所以她一生气柯南就很怕。不过她最怕的东西是妖魔鬼怪，明明有实力打倒袭击她的凶手，可是却被吓得动弹不得。她曾经多次怀疑过柯南和新一是同一个人，但是阴差阳错的被柯南瞒过去了(有时是有人帮忙的)。希望分居的父母能和好，经常计划父母的“偶遇”，但总是被拆穿。最大的愿望是新一赶快回来。但其实小小的新一就在身边……\n" +
                "\n" +
                "名字取音来源于法国著名小说家，怪盗亚森罗平的创造人莫理斯•卢布朗";
        bean3.mWidth = 589;
        bean3.mHeight = 825;
        gridBeans.add(bean3);

        StaggeredGridBean bean4 = new StaggeredGridBean();
        bean4.mPicture = "http://5b0988e595225.cdn.sohucs.com/images/20181129/8d63c789e6724c6c944e4e3908f8a9e7.jpeg";
        bean4.mName = "灰原哀";
        bean4.mContent = "真实身份是黑衣组织里的研究员，APTX4869的制药者。本名宫野志保，组织代号“雪莉(sherry)";
        bean4.mDetailContent = "班里继柯南之后又一个转学生，同为少年侦探团的一员。长得很可爱，但经常板着脸(称得上是剧中最COOL的角色)，寄宿在阿笠博士家。智商很高，精通电脑。真实身份是黑衣组织里的研究员，APTX4869的制药者。本名宫野志保，组织代号“雪莉(sherry)”。父母也是黑衣组织成员，在她小的时候就死了，留下她和姐姐两人。其姐宫野明美为救二人逃出组织而被杀害，志保因此不愿继续APTX4869的研究而被囚禁在瓦斯室。绝望的她服下了藏着的APTX4869想要自杀，没想到与柯南一样没被毒死而身体缩小成儿童时期模样，得以挣脱了手铐从垃圾通道逃了出来。因为她之前已经知道新一也变小了，所以想要向他求助，最后倒在新一家门口，被博士捡回了家。因为缺少资料，所以哀也无法制出解药，但不管怎么说都是一个希望。\n" +
                "\n" +
                "对新一很有兴趣，十分信赖他（很有进一步发展的潜力）。另外她的推理能力也不错，有时可以帮助柯南破案。“灰原哀”这个名字也是由两个侦探小说中的女主角的名字得来的。她的个性外表坚强、内心脆弱（柯南说的）。";
        bean4.mWidth = 625;
        bean4.mHeight = 926;
        gridBeans.add(bean4);

        StaggeredGridBean bean5 = new StaggeredGridBean();
        bean5.mPicture = "http://img3.a0bi.com/upload/ttq/20140705/1404531144751_middle.jpg";
        bean5.mName = "毛利小五郎";
        bean5.mContent = "小兰的父亲，职业是侦探，一个迷糊的大叔";
        bean5.mDetailContent = "小兰的父亲，职业是侦探，一个迷糊的大叔，但认真起来，也会自行解决案件。但自行推理出的案件约不到十件，大多案件都经由柯南的暗示或是柯南直接推理才得以解决。柯南经常用麻醉针将其麻醉后再用他的声音揭破案件的真相。所以毛利侦探被称为“沉睡的小五郎”（有时被笑话成“瞌睡虫小五郎”）。\n" +
                "\n" +
                "他原来是一名优秀的警局刑警，和目暮十三是同事，后来因为误伤人质(妃英理)而辞职，实质是在保护人质，不得已而为之(剧场版之<<第十四个目标>>中)，改行当私人侦探，在自家二楼开“毛利侦探事务所”，三楼为居住地。毛利小五郎的枪法十分准，柔道也是一流的，大学时代是柔道社的，被主将认为是社里最有实力的人(小兰的空手道才能也有他的遗传因素吧)。小五郎一见漂亮女性就眼睛发直，最喜欢的偶像是歌星冲野洋子小姐。另外他的吃相非常不好，没事喜欢去打麻将，或是喝啤酒（酒量不好，经常喝醉）。总之没有一点名人风度，老婆妃英理因此受不了他，在十年前就与他分居。\n" +
                "\n" +
                "每次柯南要侦查案件，小五郎总以为他要来妨碍办案，所以常在柯南头上留下一个大肿包……绝对的厉害人物…新一总是用麻醉枪麻醉他来破案～但是醒来还是会说：我就是名侦探。(不晓得会不会留下后遗症呢!)最喜欢的侦探小说是阿加莎·克里斯蒂的《无人生还》（的确，毛利大叔每到一处，必有人“不能生还”啊,人称被诅咒的名侦探~）";
        bean5.mWidth = 580;
        bean5.mHeight = 440;
        gridBeans.add(bean5);

        StaggeredGridBean bean6 = new StaggeredGridBean();
        bean6.mPicture = "http://b-ssl.duitang.com/uploads/item/201902/23/20190223113207_djgds.png";
        bean6.mName = "服部平次";
        bean6.mContent = "新一的朋友，曾在破案(外交官杀人事件)中认识的。";
        bean6.mDetailContent = "服部平次有着浓厚的关西腔，具有大阪人的豪爽与自豪感。虽然有很多柯南迷把新一和平次混为一谈，但是平次是和新一完全不同的人。平次和新一相比更像个孩子，也更加热血。也许这种特点新一也有，但平次将这样的个性放大化了。在很多人看来，这样的性子倒比稳重成熟来得更加可爱。\n" +
                "\n" +
                "大阪警察局局长服部平藏的儿子，与新一齐名的关西的高中生名侦探 称为“关西的服部，关东的工藤”。两人除发型肤色不同外 长得颇为相似（所以就有了之后多次的假扮工藤，帮工藤掩盖身份~）喜欢的推理作家为艾勒里奎恩。服部平次以前把新一当作劲敌，一直想找新一比试一下。因为新一变成了柯南，所以他自然找不到新一本人。在外交官杀人事件中，两人得以一决高低。在福尔摩斯迷事件中，平次注意到柯南的可疑迹象，最终识破了他的真实身份。此后他们就成了好友，经常联手出击，两人之间有着深厚的友谊。可是平次在开始时看到柯南都“工藤”“工藤”的乱叫，引起小兰的怀疑，还好平次人比较活络，都能够掩饰过去，即使这样柯南还是每次都会吓个半死。\n" +
                "\n" +
                "最高兴的事是和新一的推理意见有分歧的时候。喜欢用暗语嘲讽柯南跟小兰的关系。平时喜欢歪戴帽子，但一旦他戴正帽子就说明要认真思考了。钱形平次，野村胡堂笔下的侦探，有“江户的福尔摩斯”之称。最喜欢的侦探小说家是艾勒里·奎因。他经常带着青梅竹马的和叶做的护身符在身上，每次有大事发生时，总是能救他一命，似乎很灵验喔。";
        bean6.mWidth = 1280;
        bean6.mHeight = 720;
        gridBeans.add(bean6);

        StaggeredGridBean bean7 = new StaggeredGridBean();
        bean7.mPicture = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=709654579,982236118&fm=26&gp=0.jpg";
        bean7.mName = "黑羽快斗";
        bean7.mContent = "黑羽快斗，即怪盗基德，通常被叫为KID。";
        bean7.mDetailContent = "黑羽快斗，即怪盗基德，通常被叫为KID。青山刚昌漫画作品《魔术快斗》主角，《名侦探柯南》中亦正亦邪的重要角色称号有“神出鬼没变幻自在的怪盗绅士”、“月光下的魔术师”、“平成的亚森罗宾”等，但最为人熟知的还是\"KID”这个称呼。\n" +
                "\n" +
                "怪盗KID全称怪盗1412号（因为国际罪犯代码为“1412”）。充满传奇色彩的盗贼，专门以艺术品为目标的超级盗窃犯。喜欢在晚上作案，身着白色礼服，戴着特制的单片眼镜，背后的披风可以变为滑翔翼，善于各种魔术手法，精通易容，更能够不借助道具而模仿任何人的声音，打听好他人底细后，经常可以完美 的装扮成别人，不管男女老幼都可以伪装得惟妙惟肖。神出鬼没地盗取预告的目标，玩弄警察于掌中。世人称他为“月光下的魔术师”、“平成的亚森．罗宾”。举止优雅（尤其是对女性），给人一种极其绅士的影响。总而言之，魅力可是绝对不输工藤新一的。但通常偷得的宝石、书画等，不是丢弃便是归还失主。\n" +
                "\n" +
                "真实身份是高中生黑羽快斗，是个热爱魔术的少年。父亲黑羽盗一是著名的魔术师，第一代怪盗KID（有留胡子），因为偷窃珠宝卷入神秘组织中而被暗杀。著名的魔术师黑羽盗一，但在多年前去世。因为发现父亲留下的密室，并知道了父亲是被人杀害的，为了找出凶手，他继承父亲怪盗的身份。";
        bean7.mWidth = 637;
        bean7.mHeight = 825;
        gridBeans.add(bean7);

        StaggeredGridBean bean8 = new StaggeredGridBean();
        bean8.mPicture = "https://bkimg.cdn.bcebos.com/pic/ca1349540923dd54a56536bada09b3de9d8248ca?x-bce-process=image/watermark,g_7,image_d2F0ZXIvYmFpa2U5Mg==,xp_5,yp_5";
        bean8.mName = "阿笠博士";
        bean8.mContent = "自称天才发明家，热衷于科学实验，工藤新一家的邻居";
        bean8.mDetailContent = "自称天才发明家，但制作出来的很多东西其实是废铜烂铁，家中还会因为实验失败而发生小爆炸。尽管如此，阿笠博士还是有很高的发明才能，为变小后的柯南发明了很多道具，给了柯南很多帮助。\n" +
                "\n" +
                "在柯南追查组织的过程中，阿笠博士一直是柯南追查组织的得力助手和坚强后盾。\n" +
                "\n" +
                "初登场为52岁；TV 366~367中过了生日，OVA 7中自称为53岁。先后就读于帝丹小学、奥穗中学和某大学的工学部。爱车是黄色大众甲壳虫，虽然是进口车，但已改为日式。并且为了配合剧情，偶尔会换，据说车型的由来是作者青山刚昌父亲的车型。车牌是新宿 500ひ 164。喜欢的东西是银杏树，因为这是他关于初恋的美好回忆。爱好上网和发明，有不少发明界和博士界的朋友。\n" +
                "\n" +
                "作为53岁的单身汉，阿笠博士虽然没有孩子，但对小孩非常和蔼，少年侦探团的孩子们常去他家玩耍，大家也常乘坐黄色古董甲壳虫轿车一同出门去旅行。\n" +
                "\n" +
                "现与暂住在工藤家的冲矢昴是邻居。\n" +
                "\n" +
                "在剧场版中，阿笠博士有出冷笑话谜语的癖好。他的冷谜语见下面“冷谜语”部分。\n" +
                "\n" +
                "阿笠博士的屁股上的黑痣上有一根毛，不过这是只有新一才知道的秘密（TV 2）。";
        bean8.mWidth = 449;
        bean8.mHeight = 720;
        gridBeans.add(bean8);

        StaggeredGridBean bean9 = new StaggeredGridBean();
        bean9.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878513495&di=087381701a0f00870d7152a100bc176f&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2Fc9889b5c50b2e943f74bb040147381f34b86262b.png";
        bean9.mName = "赤井秀一";
        bean9.mContent = "充满神秘感的FBI搜查官。";
        bean9.mDetailContent = "充满神秘感的FBI搜查官。以“诸星大”的化名潜入黑衣组织，获得很高的地位，得到“Rye（黑麦威士忌）”的代号，被黑衣组织称为“银色子弹”；后脱离组织；假死后以冲矢昴的身份进行活动。\n" +
                "\n" +
                "年龄不详。冲矢昴的年龄介绍为27岁，但是否为赤井的真实年龄尚存疑；青山刚昌在答读者问时表示自己也不知道赤井的真实年龄。\n" +
                "\n" +
                "性格冷峻，坚定，有些自闭，眼神慑人，充满神秘感。眼下部有单褶，原来是长发（为了向组织报仇剪掉长发）。总喜欢戴着黑色的针织帽，左撇子（和琴酒相同）。\n" +
                "\n" +
                "喜欢喝波本威士忌（TV 563）、苏格兰威士忌（File.898）、罐装黑咖啡（TV 734）。爱车是雪佛兰C-1500，车牌号为新宿800 た 12-02（12月2日是其声优池田秀一的生日）。";
        bean9.mWidth = 628;
        bean9.mHeight = 393;
        gridBeans.add(bean9);

        StaggeredGridBean bean10 = new StaggeredGridBean();
        bean10.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878574001&di=b313c0be40c00b61bf0cdbefd6e67b25&imgtype=0&src=http%3A%2F%2Ftc.sinaimg.cn%2Fmaxwidth.800%2Ftc.service.weibo.com%2Fmmbiz_qpic_cn%2F7da4814853cd42b75b016eb592321ffb.jpg";
        bean10.mName = "贝尔摩德";
        bean10.mContent = "黑衣组织重要成员，真实身份是美国女明星莎朗•温亚德。";
        bean10.mDetailContent = "擅长易容术与变声术，各方面能力出众。在组织中负责收集重要的情报，是“那位先生”欣赏的人。经常单独行动，被称为“神秘主义者”。早年与工藤有希子在黑羽盗一（第一代怪盗基德）门下学习易容，成为朋友。曾易容为杀人魔后，被工藤新一、毛利兰救下。\n" +
                "\n" +
                "知道江户川柯南和灰原哀的真实身份，却并没有上报组织。认为柯南是可以摧毁组织的“银色子弹”。\n" +
                "\n" +
                "她既是荧幕上风情万种的女明星，又是为组织清除障碍的女杀手，也曾假扮成多个身份刺探情报。谜样的多重身份和种种诡异的举动总是能让人感到不寒而栗，但是对柯南和小兰又极力保护，表现出善良的一面。与琴酒相比多了份人性与神秘，少了份冷血与凶残。她的行迹和想法都无法掌握揣度，是个让人无法猜透的神秘人物。\n" +
                "\n";
        bean10.mWidth = 500;
        bean10.mHeight = 281;
        gridBeans.add(bean10);

        StaggeredGridBean bean11 = new StaggeredGridBean();
        bean11.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878629408&di=7347d004a7c8f809217d30dcbbb626e9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D5b14ee4d758da9774e2f86238050f872%2Fe6b5a9119313b07edb047d760ad7912396dd8cdd.jpg";
        bean11.mName = "波本";
        bean11.mContent = "真实身份为日本公安警察派入黑衣组织的卧底，组织代号“波本”";
        bean11.mDetailContent = "本名降谷零，化名安室透。\n" +
                "\n" +
                "真实身份为日本公安警察派入黑衣组织的卧底，组织代号“波本”（Bourbon）。表面职业为私家侦探、餐厅服务生，在毛利侦探事务所楼下的波洛咖啡厅打工，并拜毛利小五郎为师。擅长搜集情报，有极强的观察力与推理能力。与FBI搜查官赤井秀一相互厌恶，因此伪装成伤疤赤井来观察赤井秀一是否已死。\n" +
                "\n" +
                "为了调查毛利小五郎与雪莉是否有关联而接近毛利，后对柯南的身份产生了兴趣。对FBI成员进行了一系列试探，推断出赤井秀一未死和赤井假扮为冲矢昴活动。由赤井秀一揭晓其真名和日本公安警察的身份。";
        bean11.mWidth = 580;
        bean11.mHeight = 326;
        gridBeans.add(bean11);

        StaggeredGridBean bean12 = new StaggeredGridBean();
        bean12.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878672186&di=b0c50cb00555ac7d793f5d7c465ff691&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181226%2F1593a2981c4d42cb89f84ae2a5f6698c.jpeg";
        bean12.mName = "琴酒";
        bean12.mContent = "黑衣组织重要成员，经常和伏特加一起出现。";
        bean12.mDetailContent = "黑衣组织重要成员。经常和伏特加一起出现。身材高大，身穿黑色风衣，肤色偏白（很多人猜测他是混血），银色（早期为金色）长发，冰冷凌厉的双眼，墨绿色的瞳孔，左脸颊近眼睛处有被赤井秀一击伤所留下的疤痕，脸总被帽子和刘海半遮掩着，左撇子。嘴边总是少不了一支烟，外型冷峻孤傲，头脑冷静，眼神冷厉，似乎可以毫不犹豫地杀死任何人。\n" +
                "\n" +
                "经常执行各种暗杀和清除组织内奸的任务，在第1集《云霄飞车杀人事件》里，由于工藤新一偷窥到伏特加和一公司经理进行非法交易的画面，被琴酒发现后，强行给他灌下了尚在试验中的APTX4869，从此让新一身体缩小，也开始了整个故事。\n" +
                "\n" +
                "其主要任务是负责组织的安全，专门消灭组织的敌人（赤井秀一、土门康辉等）、铲除叛徒及暴露身份的组织成员（如宫野姐妹、皮斯克、爱尔兰等）。还负责重要的交易，营救值得拯救的组织成员（例如接走了被赤井击伤的贝尔摩德、被FBI软禁的基尔）。";
        bean12.mWidth = 628;
        bean12.mHeight = 374;
        gridBeans.add(bean12);

        StaggeredGridBean bean13 = new StaggeredGridBean();
        bean13.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878819968&di=16de27f5d78b1aab6f8dfb3d72856060&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F7Po3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fcc11728b4710b912c7eb7a60c3fdfc0392452201.jpg";
        bean13.mName = "冲矢昂";
        bean13.mContent = "自称是东都大学工科研究生，真实身份为FBI搜查官赤井秀一。";
        bean13.mDetailContent = "自称是东都大学工科研究生，由于原来租住的房屋在一次事件中被纵火，现暂住在工藤新一家。\n" +
                "\n" +
                "左撇子，总是眯着眼睛，身手矫健，推理能力极强，和安室透（波本）是对手。\n" +
                "\n" +
                "知晓柯南真实身份，经常协助柯南等人，柯南对他也非常信任。TV 783（File.898）确认其真实身份为FBI搜查官赤井秀一。赤井在设计假死后以冲矢昴身份出现，目的是暗中保护灰原哀。";
        bean13.mWidth = 628;
        bean13.mHeight = 358;
        gridBeans.add(bean13);

        StaggeredGridBean bean14 = new StaggeredGridBean();
        bean14.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589878940969&di=d079a95716ee4bb77869517d3fa3f8bd&imgtype=0&src=http%3A%2F%2Fx0.ifengimg.com%2Fres%2F2019%2F82BC88FCD07F78BDC3D165BF31846AC2D4D4A91D_size54_w1024_h577.jpeg";
        bean14.mName = "京极真";
        bean14.mContent = "全日本空手道冠军，有“蹴击贵公子”的称号";
        bean14.mDetailContent = "全日本空手道冠军，有“蹴击贵公子”的称号。在某次空手道比赛中看见铃木园子为好友毛利兰拼命加油的身姿之后便开始喜欢园子。之后园子和兰、柯南在伊豆度假中，京极真亲手救下了受杀人犯威胁的园子，并向园子表白。之后去国外留学，因而平常很少和园子碰面，平时主要凭借电话和园子联系。但是多次在园子遇到危难的时候及时出现并救了她，在TV 458的时候竟然为了等园子放弃401连胜。\n" +
                "\n" +
                " \n" +
                "\n" +
                "他人评价：\n" +
                "\n" +
                "铃木园子：\n" +
                "\n" +
                "与其说阿真他像个王子，不如说更像是个满身伤痕的武士。（TV 172）\n" +
                "\n" +
                "他是个又帅气又温柔的好男人！！（TV 744）\n" +
                "\n" +
                " \n" +
                "\n" +
                "江户川柯南：\n" +
                "\n" +
                "这个男人早晚会因为破坏公物而被抓的。（TV 744）\n" +
                "\n" +
                " \n" +
                "\n" +
                "铃木史郎：\n" +
                "\n" +
                "这小伙子挺可靠的嘛！（TV 747）\n" +
                "\n" +
                " \n" +
                "\n" +
                "铃木朋子：\n" +
                "\n" +
                "是个认真而朴实，谦逊却强韧的男人……值得调教……（TV 747）";
        bean14.mWidth = 628;
        bean14.mHeight = 354;
        gridBeans.add(bean14);

        StaggeredGridBean bean15 = new StaggeredGridBean();
        bean15.mPicture = "https://bkimg.cdn.bcebos.com/pic/37d12f2eb9389b5085487a808e35e5dde6116ea9?x-bce-process=image/watermark,g_7,image_d2F0ZXIvYmFpa2U4MA==,xp_5,yp_5";
        bean15.mName = "铃木园子";
        bean15.mContent = "是铃木财团的二小姐，个性却大大咧咧";
        bean15.mDetailContent = "就读于帝丹高中二年B班的女高中生，工藤新一和毛利兰的同班同学，小兰的闺蜜。出身名门，是铃木财团的二小姐，个性却大大咧咧。\n" +
                "经常被柯南用麻醉枪弄睡后，借用她的嘴巴推理案情然后屡破奇案，人称“推理女王”。但因为是睡着的关系，所以根本不记得推理案情的经过，只是怀疑自己是“左脑使用过度，所以右脑失去记忆了”的样子。";
        bean15.mWidth = 377;
        bean15.mHeight = 720;
        gridBeans.add(bean15);

        StaggeredGridBean bean16 = new StaggeredGridBean();
        bean16.mPicture = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589879145482&di=02fd3d3f30e69427248b306115bf9795&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171018%2F76f3b23898aa407a879ab9cd95e0aef2.jpeg";
        bean16.mName = "妃英理";
        bean16.mContent = "著名律师，拥有“律政界女王”的称号。与毛利小五郎分居中。";
        bean16.mDetailContent = "毛利小五郎的妻子，毛利兰的母亲，工藤有希子的好友。著名律师，拥有“律政界女王”的称号。与毛利小五郎分居中。";
        bean16.mWidth = 530;
        bean16.mHeight = 335;
        gridBeans.add(bean16);

        return gridBeans;
    }
}
