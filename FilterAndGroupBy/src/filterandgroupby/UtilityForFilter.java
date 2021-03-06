/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filterandgroupby;
import static filterandgroupby.Main.inputs;
import static filterandgroupby.Main.result;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
/**
 *
 * @author admin
 */
public class UtilityForFilter {
    LinkedList<Integer>result=new LinkedList<Integer>();
    static List<ConditionsEnum>conditions=new LinkedList<ConditionsEnum>();
    static Map<ConditionsEnum,BiFunction<Value,String,Boolean>>conditionMap=new EnumMap<ConditionsEnum,BiFunction<Value,String,Boolean>>(ConditionsEnum.class);
   private Boolean equals(Value v,String condition)
   {
         if(!v.type.equals(DataTypes.String))
         {
             double val=Double.valueOf(condition);
             if(val==(double)v.getValue())
                return true;
        }
        else if(v.getValue().equals(condition))
        {
             return true;
        }  
         return false;
   }
   private Boolean greaterThan(Value v,String condition) 
   {
       if(!v.type.equals(DataTypes.String)&&(double)v.getValue()>Double.valueOf(condition))
       {
           return true;
       }
       return false;
   }
   private Boolean lessThan(Value v,String condition)
   {
       if(!v.type.equals(DataTypes.String)&&(double)v.getValue()<Double.valueOf(condition))
       {
           return true;
       }
       return false;
   }
   private Boolean beginsWith(Value v,String condition)
   {
       if(v.type.equals(DataTypes.String))
       {    
           String temp=v.getValue().toString();
           if(temp.startsWith(condition))
           return true;
       }
       return false;
   }
   private Boolean contains(Value v,String condition)
   {
       if(v.type.equals(DataTypes.String))
       {
            String temp=v.getValue().toString();
           for(int i=0;i<condition.length();i++)
           {
               if(temp.indexOf(condition.charAt(i))==-1)
                   return false;
           }
           return true;
       }
       return false;
   }
   private Boolean endsWith(Value v,String condition)
   {
       if(v.type.equals(DataTypes.String))
       {
           String temp=v.getValue().toString();
           if(temp.endsWith(condition))
           return true;
       }
       return false;
   }
   static{

        UtilityForFilter util=new UtilityForFilter();
                    conditions.add(ConditionsEnum.Equals);
                    conditions.add(ConditionsEnum.DoesNotEquals);
                    conditions.add(ConditionsEnum.GreaterThan);
                    conditions.add(ConditionsEnum.GreaterThanOrEqualTo);
                    conditions.add(ConditionsEnum.LessThan);
                    conditions.add(ConditionsEnum.LessThanOrEqualTo);
                    conditions.add(ConditionsEnum.BeginesWith);
                    conditions.add(ConditionsEnum.DoesNotBeginWith);
                    conditions.add(ConditionsEnum.EndsWith);
                    conditions.add(ConditionsEnum.DoesNotEndsWith);
                    conditions.add(ConditionsEnum.Contains);
                    conditions.add(ConditionsEnum.DoesNotContains);
                    conditions.add(ConditionsEnum.Matches);
                    conditions.add(ConditionsEnum.DoesNotMatchs);
                    conditionMap.put(ConditionsEnum.Equals, (v1,v2) -> util.equals(v1,v2));
                    conditionMap.put(ConditionsEnum.DoesNotEquals, (v1,v2) -> !util.equals(v1,v2));
                    conditionMap.put(ConditionsEnum.GreaterThan, (v1,v2) -> util.greaterThan(v1,v2));
                    conditionMap.put(ConditionsEnum.GreaterThanOrEqualTo, (v1,v2) -> !util.lessThan(v1,v2));
                    conditionMap.put(ConditionsEnum.LessThan, (v1,v2) -> util.lessThan(v1,v2));
                    conditionMap.put(ConditionsEnum.LessThanOrEqualTo, (v1,v2)-> !util.greaterThan(v1,v2));
                    conditionMap.put(ConditionsEnum.BeginesWith, (v1,v2)-> !util.beginsWith(v1,v2));
                    conditionMap.put(ConditionsEnum.DoesNotBeginWith, (v1,v2)-> util.beginsWith(v1,v2));
                    conditionMap.put(ConditionsEnum.EndsWith, (v1,v2)-> !util.endsWith(v1,v2));
                    conditionMap.put(ConditionsEnum.DoesNotEndsWith, (v1,v2)-> util.endsWith(v1,v2));
                    conditionMap.put(ConditionsEnum.Contains,(v1,v2)-> util.contains(v1,v2));
                    conditionMap.put(ConditionsEnum.DoesNotContains,(v1,v2) -> !util.contains(v1,v2));
                    conditionMap.put(ConditionsEnum.Matches,(v1,v2) -> util.equals(v1,v2));
                    conditionMap.put(ConditionsEnum.DoesNotMatchs,(v1,v2)-> !util.equals(v1,v2));
   }
    
    public LinkedList<Integer> filter(Range r,LinkedList<Condition>conList)
    {   
        
        List<Integer>input=r.getVisibleRows();
        for(Integer row:input)
        {
            int count=0;
            for(Condition c:conList)
            {
                Value v=(Value) inputs.get(row).get(c.col);
                BiFunction<Value,String,Boolean> test =conditionMap.get(conditions.get(c.conNo));
                if(test.apply(v, c.condition))
                    count++;
                else
                    break;
            }
            if(count==conList.size()-1)
                result.add(row);
        }
     Filter f=new Filter(conList,result);
     Range.addResult(f);
     return result;
    }
}
