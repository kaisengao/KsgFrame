package com.kasiengao.ksgframe.java.staggered;

import com.kasiengao.ksgframe.java.element.PreviewBean;

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

        gridBeans.add(createBean0());
        gridBeans.add(createBean1());
        gridBeans.add(createBean2());
        gridBeans.add(createBean3());
        gridBeans.add(createBean4());
        gridBeans.add(createBean5());
        gridBeans.add(createBean6());
        gridBeans.add(createBean7());
        gridBeans.add(createBean8());

        return gridBeans;
    }

    private StaggeredGridBean createBean0() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "蜘蛛侠：英雄远征 内地定档预告";
        gridBean.mContent = "《蜘蛛侠：英雄远征》定档预告";
        gridBean.mDetailContent = "《蜘蛛侠：英雄远征》定档预告";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(1000, 562, "video",
                "http://img5.mtime.cn/mg/2019/05/31/164845.22030068_120X90X4.jpg",
                "http://vfx.mtime.cn/Video/2019/05/31/mp4/190531170200490672.mp4"));

        previewBeans.add(createPreview(700, 350, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589973046620&di=a1bea927f4af23b0791f7f0ca93fdf1d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201411%2F27%2F20141127112915_PPPFV.thumb.700_0.gif"));

        previewBeans.add(createPreview(474, 842, "image",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2259647140,2312250263&fm=26&gp=0.jpg"));

        previewBeans.add(createPreview(1000, 562, "video",
                "http://img5.mtime.cn/mg/2019/05/31/164845.22030068_120X90X4.jpg",
                "http://vfx.mtime.cn/Video/2019/05/31/mp4/190531170200490672.mp4"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean1() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "江户川柯南";
        gridBean.mContent = "故事里的男主角，“名侦探柯南”的灵魂人物，原名工藤新一";
        gridBean.mDetailContent = "他是故事里的男主角，“名侦探柯南”的灵魂人物。原名工藤新一，帝丹高中二年极学生，与小兰是青梅竹马。继承了父亲的超强推理能力，帮助警方破获了不少案子，被誉为“日本警察的救世主”。他喜欢看推理小说，最崇拜福尔摩斯，踢足球的水平一流，擅长凌空抽射。被卷入黑衣组织的交易被灌下毒药APTX4869，幸运的是并没有死，只是退化成了小孩，目前年龄定为7岁。改名为江户川柯南。智力没有衰退，推理能力丝毫未减。\n" +
                "\n" +
                "为了找出黑衣人的下落，寄住在家开侦探事务所的小兰家，一方面暗地协助小五郎破案。而体力上的不足靠阿笠博士的发明来弥补。变小后就读于帝丹小学1年级B班，认识了吉田步美、小岛元太、圆谷光彦三人，以及后来加入的灰原哀，成立了少年侦探团，是其中最重要的“首脑”。不管是变小前后，唱歌五音不全还是没有办法变更的事实……唱歌走音非常夸张。";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(700, 350, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589973046620&di=a1bea927f4af23b0791f7f0ca93fdf1d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201411%2F27%2F20141127112915_PPPFV.thumb.700_0.gif"));

        previewBeans.add(createPreview(474, 842, "image",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2259647140,2312250263&fm=26&gp=0.jpg"));

        previewBeans.add(createPreview(700, 319, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589973046620&di=a1bea927f4af23b0791f7f0ca93fdf1d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201411%2F27%2F20141127112915_PPPFV.thumb.700_0.gif"));

        previewBeans.add(createPreview(580, 319, "image",
                "http://imgsrc.baidu.com/forum/w=580/sign=c76377ecb2b7d0a27bc90495fbee760d/537cf3deb48f8c54feb9de713a292df5e0fe7f37.jpg"));

        previewBeans.add(createPreview(800, 480, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589952204408&di=7d2762e7dbf5d6127c5aa6ee641f4333&imgtype=0&src=http%3A%2F%2Fsinastorage.com%2Fdata.ent.sina.com.cn%2Fmovie%2Fstills%2F9801%2F2b83c1717f6515c1d165979b58360610.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean2() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "工藤新一";
        gridBean.mContent = "帝丹高中二年级2班学生、高中生侦探";
        gridBean.mDetailContent = "漫画《名侦探柯南》及衍生作品的主人公和《魔术快斗》中的客串角色。17岁 [1-2]  ，高中生侦探，就读于帝丹高中二年级B班，人称“平成年代的福尔摩斯”“日本警察的救世主”。\n" +
                "在和青梅竹马毛利兰一同去游乐园玩时，目睹黑衣组织成员秘密交易时而被偷袭，并被灌下名为“APTX4869”的毒药，虽然幸免于死，但身体就此缩小成7岁小学生的模样。之后寻求阿笠博士的帮助，在被小兰询问自己名字时，化名为江户川柯南。在阿笠博士的提议下，寄住于小兰的父亲、私家侦探毛利小五郎家中，秘密调查黑衣组织";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(320, 171, "image",
                "http://imgsrc.baidu.com/forum/w=580/sign=ec05b6d2c75c1038247ececa8210931c/199c2834349b033b5ee04a1214ce36d3d739bdc0.jpg"));

        previewBeans.add(createPreview(1600, 900, "image",
                "http://b-ssl.duitang.com/uploads/item/201901/22/20190122005154_KuvtH.jpeg"));

        previewBeans.add(createPreview(305, 423, "image",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=413318692,257680391&fm=26&gp=0.jpg"));

        previewBeans.add(createPreview(600, 319, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589961626693&di=2906d984505d4788db10057fc6e0495a&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2Fbf8f73ccb5cb83078478dcb10988c868efe6ef36.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean3() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "毛利兰";
        gridBean.mContent = "毛利兰是故事里的女主角，帝丹高中2年级生，和新一是同班同学";
        gridBean.mDetailContent = "毛利兰是故事里的女主角，帝丹高中2年级生，和新一是同班同学，血型和新一相同，对他有着超越一般朋友的感情但又不愿承认。好友为园子及和叶。她是一个坚强又温柔的女孩，擅长料理家事。因崇拜空手道冠军前田聪，便开始学习空手道且对空手道有着浓厚的兴趣，功夫也不浅，为空手道社的主将，还曾经获得东京空手道大会的冠军。所以她一生气柯南就很怕。不过她最怕的东西是妖魔鬼怪，明明有实力打倒袭击她的凶手，可是却被吓得动弹不得。她曾经多次怀疑过柯南和新一是同一个人，但是阴差阳错的被柯南瞒过去了(有时是有人帮忙的)。希望分居的父母能和好，经常计划父母的“偶遇”，但总是被拆穿。最大的愿望是新一赶快回来。但其实小小的新一就在身边……\n" +
                "\n" +
                "名字取音来源于法国著名小说家，怪盗亚森罗平的创造人莫理斯•卢布朗";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(656, 352, "image",
                "http://5b0988e595225.cdn.sohucs.com/images/20171109/2dd3a52689364b6e8fe686ce81acec41.gif"));

        previewBeans.add(createPreview(589, 825, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589881905450&di=f1aac7c4aa845dab974c4424d27bbff2&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9fo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fcaef76094b36acaf547a5f1f7cd98d1000e99cbf.jpg"));

        previewBeans.add(createPreview(1280, 720, "image",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2453668639,3999103028&fm=26&gp=0.jpg"));

        previewBeans.add(createPreview(512, 512, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589961769039&di=4a476b9010739cae0e11b8c637ed02d5&imgtype=0&src=http%3A%2F%2Fimg5.iqiyipic.com%2Fimage%2Fppopen%2Fppopen_5a84b76a14d4557a78ac772c_0.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean4() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "灰原哀";
        gridBean.mContent = "真实身份是黑衣组织里的研究员，APTX4869的制药者。本名宫野志保，组织代号“雪莉(sherry)";
        gridBean.mDetailContent = "班里继柯南之后又一个转学生，同为少年侦探团的一员。长得很可爱，但经常板着脸(称得上是剧中最COOL的角色)，寄宿在阿笠博士家。智商很高，精通电脑。真实身份是黑衣组织里的研究员，APTX4869的制药者。本名宫野志保，组织代号“雪莉(sherry)”。父母也是黑衣组织成员，在她小的时候就死了，留下她和姐姐两人。其姐宫野明美为救二人逃出组织而被杀害，志保因此不愿继续APTX4869的研究而被囚禁在瓦斯室。绝望的她服下了藏着的APTX4869想要自杀，没想到与柯南一样没被毒死而身体缩小成儿童时期模样，得以挣脱了手铐从垃圾通道逃了出来。因为她之前已经知道新一也变小了，所以想要向他求助，最后倒在新一家门口，被博士捡回了家。因为缺少资料，所以哀也无法制出解药，但不管怎么说都是一个希望。\n" +
                "\n" +
                "对新一很有兴趣，十分信赖他（很有进一步发展的潜力）。另外她的推理能力也不错，有时可以帮助柯南破案。“灰原哀”这个名字也是由两个侦探小说中的女主角的名字得来的。她的个性外表坚强、内心脆弱（柯南说的）。";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(356, 200, "image",
                "http://c-ssl.duitang.com/uploads/item/201506/24/20150624194258_5RMSk.thumb.400_0.gif"));

        previewBeans.add(createPreview(625, 926, "image",
                "http://5b0988e595225.cdn.sohucs.com/images/20181129/8d63c789e6724c6c944e4e3908f8a9e7.jpeg"));

        previewBeans.add(createPreview(1010, 568, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589952104171&di=d75b698337336d90b459119a7f0fc104&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2F20170829%2F498b-fykkfat1678123.jpg"));

        previewBeans.add(createPreview(700, 395, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589952152377&di=b1975136fb43df03abcad546e29c64a7&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F25%2F20170825171630_5cW8E.thumb.700_0.png"));

        previewBeans.add(createPreview(700, 738, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589952522574&di=7d5726c3aee21213519535bbf93d8a6a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2F1c%2F90df1355930.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean5() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "毛利小五郎";
        gridBean.mContent = "小兰的父亲，职业是侦探，一个迷糊的大叔";
        gridBean.mDetailContent = "小兰的父亲，职业是侦探，一个迷糊的大叔，但认真起来，也会自行解决案件。但自行推理出的案件约不到十件，大多案件都经由柯南的暗示或是柯南直接推理才得以解决。柯南经常用麻醉针将其麻醉后再用他的声音揭破案件的真相。所以毛利侦探被称为“沉睡的小五郎”（有时被笑话成“瞌睡虫小五郎”）。\n" +
                "\n" +
                "他原来是一名优秀的警局刑警，和目暮十三是同事，后来因为误伤人质(妃英理)而辞职，实质是在保护人质，不得已而为之(剧场版之<<第十四个目标>>中)，改行当私人侦探，在自家二楼开“毛利侦探事务所”，三楼为居住地。毛利小五郎的枪法十分准，柔道也是一流的，大学时代是柔道社的，被主将认为是社里最有实力的人(小兰的空手道才能也有他的遗传因素吧)。小五郎一见漂亮女性就眼睛发直，最喜欢的偶像是歌星冲野洋子小姐。另外他的吃相非常不好，没事喜欢去打麻将，或是喝啤酒（酒量不好，经常喝醉）。总之没有一点名人风度，老婆妃英理因此受不了他，在十年前就与他分居。\n" +
                "\n" +
                "每次柯南要侦查案件，小五郎总以为他要来妨碍办案，所以常在柯南头上留下一个大肿包……绝对的厉害人物…新一总是用麻醉枪麻醉他来破案～但是醒来还是会说：我就是名侦探。(不晓得会不会留下后遗症呢!)最喜欢的侦探小说是阿加莎·克里斯蒂的《无人生还》（的确，毛利大叔每到一处，必有人“不能生还”啊,人称被诅咒的名侦探~）";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(400, 225, "image",
                "http://img.mp.itc.cn/upload/20160804/539e42bc492149cb9a2b6ec5f17766cd.jpg"));

        previewBeans.add(createPreview(580, 440, "image",
                "http://img3.a0bi.com/upload/ttq/20140705/1404531144751_middle.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean6() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "服部平次";
        gridBean.mContent = "新一的朋友，曾在破案(外交官杀人事件)中认识的。";
        gridBean.mDetailContent = "服部平次有着浓厚的关西腔，具有大阪人的豪爽与自豪感。虽然有很多柯南迷把新一和平次混为一谈，但是平次是和新一完全不同的人。平次和新一相比更像个孩子，也更加热血。也许这种特点新一也有，但平次将这样的个性放大化了。在很多人看来，这样的性子倒比稳重成熟来得更加可爱。\n" +
                "\n" +
                "大阪警察局局长服部平藏的儿子，与新一齐名的关西的高中生名侦探 称为“关西的服部，关东的工藤”。两人除发型肤色不同外 长得颇为相似（所以就有了之后多次的假扮工藤，帮工藤掩盖身份~）喜欢的推理作家为艾勒里奎恩。服部平次以前把新一当作劲敌，一直想找新一比试一下。因为新一变成了柯南，所以他自然找不到新一本人。在外交官杀人事件中，两人得以一决高低。在福尔摩斯迷事件中，平次注意到柯南的可疑迹象，最终识破了他的真实身份。此后他们就成了好友，经常联手出击，两人之间有着深厚的友谊。可是平次在开始时看到柯南都“工藤”“工藤”的乱叫，引起小兰的怀疑，还好平次人比较活络，都能够掩饰过去，即使这样柯南还是每次都会吓个半死。\n" +
                "\n" +
                "最高兴的事是和新一的推理意见有分歧的时候。喜欢用暗语嘲讽柯南跟小兰的关系。平时喜欢歪戴帽子，但一旦他戴正帽子就说明要认真思考了。钱形平次，野村胡堂笔下的侦探，有“江户的福尔摩斯”之称。最喜欢的侦探小说家是艾勒里·奎因。他经常带着青梅竹马的和叶做的护身符在身上，每次有大事发生时，总是能救他一命，似乎很灵验喔。";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(503, 281, "image",
                "http://imgsrc.baidu.com/forum/w=580/sign=793f4d3508f79052ef1f47363cf2d738/d3ead7ca7bcb0a46ab84b51f6a63f6246b60af0f.jpg"));

        previewBeans.add(createPreview(1280, 720, "image",
                "http://b-ssl.duitang.com/uploads/item/201902/23/20190223113207_djgds.png"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean7() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "黑羽快斗";
        gridBean.mContent = "黑羽快斗，即怪盗基德，通常被叫为KID。";
        gridBean.mDetailContent = "黑羽快斗，即怪盗基德，通常被叫为KID。青山刚昌漫画作品《魔术快斗》主角，《名侦探柯南》中亦正亦邪的重要角色称号有“神出鬼没变幻自在的怪盗绅士”、“月光下的魔术师”、“平成的亚森罗宾”等，但最为人熟知的还是\"KID”这个称呼。\n" +
                "\n" +
                "怪盗KID全称怪盗1412号（因为国际罪犯代码为“1412”）。充满传奇色彩的盗贼，专门以艺术品为目标的超级盗窃犯。喜欢在晚上作案，身着白色礼服，戴着特制的单片眼镜，背后的披风可以变为滑翔翼，善于各种魔术手法，精通易容，更能够不借助道具而模仿任何人的声音，打听好他人底细后，经常可以完美 的装扮成别人，不管男女老幼都可以伪装得惟妙惟肖。神出鬼没地盗取预告的目标，玩弄警察于掌中。世人称他为“月光下的魔术师”、“平成的亚森．罗宾”。举止优雅（尤其是对女性），给人一种极其绅士的影响。总而言之，魅力可是绝对不输工藤新一的。但通常偷得的宝石、书画等，不是丢弃便是归还失主。\n" +
                "\n" +
                "真实身份是高中生黑羽快斗，是个热爱魔术的少年。父亲黑羽盗一是著名的魔术师，第一代怪盗KID（有留胡子），因为偷窃珠宝卷入神秘组织中而被暗杀。著名的魔术师黑羽盗一，但在多年前去世。因为发现父亲留下的密室，并知道了父亲是被人杀害的，为了找出凶手，他继承父亲怪盗的身份。";

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(350, 197, "image",
                "http://imgsrc.baidu.com/forum/w=580/sign=2bcdbe31d8b44aed594ebeec831d876a/fb1e6a2309f7905235f7fa450ef3d7ca7acbd518.jpg"));

        previewBeans.add(createPreview(637, 825, "image",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=709654579,982236118&fm=26&gp=0.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    private StaggeredGridBean createBean8() {
        StaggeredGridBean gridBean = new StaggeredGridBean();

        gridBean.mName = "阿笠博士";
        gridBean.mContent = "自称天才发明家，热衷于科学实验，工藤新一家的邻居";
        gridBean.mDetailContent = "自称天才发明家，但制作出来的很多东西其实是废铜烂铁，家中还会因为实验失败而发生小爆炸。尽管如此，阿笠博士还是有很高的发明才能，为变小后的柯南发明了很多道具，给了柯南很多帮助。\n" +
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

        List<PreviewBean> previewBeans = new ArrayList<>();

        previewBeans.add(createPreview(700, 954, "image",
                "http://b-ssl.duitang.com/uploads/item/201706/23/20170623192358_AYzKE.jpeg"));

        previewBeans.add(createPreview(640, 492, "image",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589874514907&di=8f60b59bf189b148beaad860c5a48f83&imgtype=0&src=http%3A%2F%2Fvpic.video.qq.com%2F3388556%2Ft0545sibsex_ori_3.jpg"));

        gridBean.mPreviewBeans = previewBeans;

        return gridBean;
    }

    /**
     * 生成 预览数据
     *
     * @param width  资源高度
     * @param height 资源宽度
     */
    private PreviewBean createPreview(int width, int height, String type, String pictureUrl) {
        return this.createPreview(width, height, type, pictureUrl, "");
    }

    /**
     * 生成 预览数据
     *
     * @param width  资源高度
     * @param height 资源宽度
     */
    private PreviewBean createPreview(int width, int height, String type, String pictureUrl, String videoUrl) {
        PreviewBean previewBean = new PreviewBean();
        previewBean.mWidth = width;
        previewBean.mHeight = height;
        previewBean.mMediaType = type;
        previewBean.mPictureUrl = pictureUrl;
        previewBean.mVideoUrl = videoUrl;
        return previewBean;
    }
}
