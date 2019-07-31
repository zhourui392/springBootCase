package cn.zz.user.stream;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : zhourui
 * @version: 2019-07-22 19:10
 **/
public class StreamTest {
    List<Human> humans =null;

    @Before
    public void init(){
        humans = Stream.of(
                new Human("Sarah","female", 31, 1),
                new Human("Mary","female", 12, 2),
                new Human("Messi","male", 31, 3),
                new Human("Ronaldo","male", 33, 4),
                new Human("Ronaldo","male", 43, 5))
                .collect(Collectors.toList());
    }

    //过滤
    @Test
    public void filter(){
        //获取姓名为Ronaldo的人
        List<Human> nhumans = humans.stream()
                        .filter(ex -> ex.getName().equals("Ronaldo"))
                        .collect(Collectors.toList());
        System.out.println(nhumans);
        //结果：[Human [name=Ronaldo, sex=male, age=33, id=4], Human [name=Ronaldo, sex=male, age=43, id=5]]

        //获取年龄31的人
        nhumans = humans.stream()
                .filter(it->it.getAge()==31)
                .collect(Collectors.toList());
        System.out.println(nhumans);
        //结果：[Human [name=Sarah, sex=female, age=31, id=1], Human [name=Messi, sex=male, age=31, id=3]]

        //过滤后获取第一个，按照put的顺序取
        Optional<Human> humanOps = humans.stream()
                .filter(it->it.getAge()==31)
                .findFirst();
        Human human =  humanOps.isPresent() ? humanOps.get() : null;
        System.out.println(human);
        //结果：Human [name=Sarah, sex=female, age=31, id=1]
    }

    // 聚合
    @Test
    public void aggregate() {
        // 获取最大年龄
        int maxage = humans.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Human::getAge)))
                .get()
                .getAge();
        System.out.println(maxage);// 结果：43

        // 获取最小年龄
        int minage = humans.stream()
                .collect(Collectors.minBy(Comparator.comparingInt(Human::getAge)))
                .get()
                .getAge();
        System.out.println(minage);// 结果：12

        // 获取平均年龄
        double avgAge = humans.stream()
                .collect(Collectors.averagingInt(Human::getAge));
        System.out.println(avgAge);// 结果：30
        // 获取总年龄,方法1
        int sumAge = humans.stream()
                .collect(Collectors.summingInt((Human::getAge)));
        System.out.println(sumAge);// 结果：150
        // 获取总年龄,方法2
        sumAge = humans.stream()
                .collect(Collectors.reducing(0, Human::getAge, Integer::sum));
        System.out.println(sumAge);// 结果：150
        // 获取总年龄,方法3
        sumAge = humans.stream()
                .mapToInt(Human::getAge)
                .sum();
        System.out.println(sumAge);// 结果：150
        // 获取总年龄,方法4,并行处理
        sumAge = humans.stream()
                .parallel()
                .map(Human::getAge) //
                .reduce( 0, Integer::sum );
        System.out.println(sumAge);// 结果：150

    }

    //排序
    @Test
    public void sort(){
        //从小到大排序，实现方式1
        List<Human> nhumans = humans.stream()
                .sorted(Comparator.comparingInt(r->r.getAge()))
                .collect(Collectors.toList());
        System.out.println(nhumans);
        //从小到大排序，实现方式2
        nhumans = humans.stream()
                .sorted(Comparator.comparing(Human::getAge))
                .collect(Collectors.toList());
        System.out.println(nhumans);
        //从小到大排序，实现方式3
        humans.sort((h1, h2) -> Integer.compare(h1.getAge(),h2.getAge()));
        System.out.println(humans);
        //结果：[Human [name=Mary, sex=female, age=12, id=2], Human [name=Sarah, sex=female, age=31, id=1],
        //Human [name=Messi, sex=male, age=31, id=3], Human [name=Ronaldo, sex=male, age=33, id=4],
        //Human [name=Ronaldo, sex=male, age=43, id=5]]

        //先按照年龄排序，第二优先级按照姓名排序
        humans.sort(
                Comparator.comparing(Human::getAge).thenComparing(Human::getName)
        );
        System.out.println(humans);
        //结果：[Human [name=Mary, sex=female, age=12, id=2], Human [name=Messi, sex=male, age=31, id=3],
        //Human [name=Sarah, sex=female, age=31, id=1], Human [name=Ronaldo, sex=male, age=33, id=4],
        //Human [name=Ronaldo, sex=male, age=43, id=5]]


        //从大到小排序
        nhumans = humans.stream()
                .sorted(Comparator.comparing(Human::getAge).reversed()).collect(Collectors.toList());
        System.out.println(nhumans);
        //结果：[Human [name=Ronaldo, sex=male, age=43, id=5], Human [name=Ronaldo, sex=male, age=33, id=4],
        //Human [name=Sarah, sex=female, age=31, id=1], Human [name=Messi, sex=male, age=31, id=3],
        //Human [name=Mary, sex=female, age=12, id=2]]

    }


    //类型转换
    @Test
    public void transfer(){
        ///按性别分组成子list
        Map<String, List<Human>> vgroup = humans.stream()
                .collect(Collectors.groupingBy(Human::getSex));
        System.out.println(vgroup);
        //结果：{female=[Human [name=Sarah, sex=female, age=31, id=1], Human [name=Mary, sex=female, age=12, id=2]],
        //male=[Human [name=Messi, sex=male, age=31, id=3], Human [name=Ronaldo, sex=male, age=33, id=4], Human [name=Ronaldo, sex=male, age=43, id=5]]}

        //转成map，key为id,方法1
        Map<Long, Human> map = humans.stream()
                .collect(Collectors.toMap(Human::getId, it->it));
        System.out.println(map);
        //转成map，key为id,方法2
        map = humans.stream()
                .collect(Collectors.toMap(Human::getId, Function.identity()));
        System.out.println(map);
        //结果：{1=Human [name=Sarah, sex=female, age=31, id=1], 2=Human [name=Mary, sex=female, age=12, id=2],
        //3=Human [name=Messi, sex=male, age=31, id=3], 4=Human [name=Ronaldo, sex=male, age=33, id=4],
        //5=Human [name=Ronaldo, sex=male, age=43, id=5]}

        //转成map，key为name,name有重复，会报错
        Map<String, Human> smap;
        try {
            smap = humans.stream()
                    .collect(Collectors.toMap(Human::getName, Function.identity()));
            System.out.println(smap);
        } catch (Exception e) {
            e.printStackTrace();
            //报错：java.lang.IllegalStateException: Duplicate key Human [name=Ronaldo, sex=male, age=33, id=4]
            //at java.util.stream.Collectors.lambda$throwingMerger$0(Collectors.java:133)
        }
        //转成map，key为name,name有重复，出现重复key时，后面的值覆盖前面的值
        smap = humans.stream()
                .collect(Collector.of(HashMap::new, (m, a) -> m.put(a.getName(), a),
                        (k, v) -> v, Collector.Characteristics.IDENTITY_FINISH));
        System.out.println(smap);
        //结果：{Ronaldo=Human [name=Ronaldo, sex=male, age=43, id=5],
        //Sarah=Human [name=Sarah, sex=female, age=31, id=1], Messi=Human [name=Messi, sex=male, age=31, id=3],
        //Mary=Human [name=Mary, sex=female, age=12, id=2]}

        //抽取name字段
        List<String> names = humans.stream()
                .map(Human::getName)
                .collect(Collectors.toList());
        System.out.println(names);
        //结果：[Sarah, Mary, Messi, Ronaldo, Ronaldo]

        //抽取name字段,去重
        names = humans.stream()
                .map(Human::getName)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(names);
        //结果：[Sarah, Mary, Messi, Ronaldo]

    }

    //迭代处理
    @Test
    public void foreach(){
        humans.stream().forEach(it->it.setName("hello:"+it.getName()));
        System.out.println(humans);
        //结果：[Human [name=hello:Sarah, sex=female, age=31, id=1], Human [name=hello:Mary, sex=female, age=12, id=2],
        //Human [name=hello:Messi, sex=male, age=31, id=3], Human [name=hello:Ronaldo, sex=male, age=33, id=4],
        //Human [name=hello:Ronaldo, sex=male, age=43, id=5]]

        humans.stream().forEach(it -> {
            it.setSex("sex:"+it.getSex());
            it.setAge(10+it.getAge());
        });
        System.out.println(humans);
        //结果：[Human [name=hello:Sarah, sex=sex:female, age=41, id=1], Human [name=hello:Mary, sex=sex:female, age=22, id=2],
        //Human [name=hello:Messi, sex=sex:male, age=41, id=3], Human [name=hello:Ronaldo, sex=sex:male, age=43, id=4],
        //Human [name=hello:Ronaldo, sex=sex:male, age=53, id=5]]
    }

    //并行处理
    @Test
    public void parallel(){
        //先按年龄从小到大排序
        List<Human> nhumans = humans.stream()
                .sorted(Comparator.comparingInt(r->r.getAge()))
                .collect(Collectors.toList());

        //串行处理，有顺序
        StringBuffer rs = new StringBuffer();
        nhumans.stream().forEach(it->rs.append(it.getAge()).append(","));
        System.out.println(rs.toString());
        //结果：12,31,31,33,43,

        //并行处理，结果无序的：
        StringBuffer prs = new StringBuffer();
        nhumans.parallelStream().forEach(it->prs.append(it.getAge()).append(","));
        System.out.println(prs.toString());
        //结果：31,33,43,31,12,

        //并行求和
        int sum = nhumans.parallelStream()
                .mapToInt(s -> s.getAge())
                .sum();
        System.out.println(sum);
    }

}
