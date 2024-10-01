package com.hitecart.deliverysystem.interceptor;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interceptor implements StatementInspector {
    @Override
    public String inspect(String sql) {
        System.out.println(sql);
//        if(StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")){
//            final String entityName = getTableNameFromQuery(sql);
//            String aliasTab = deriveAlias(sql);
//            if(StringUtils.hasLength(entityName)){
//                if(sql.contains("where")){
//                    sql =sql +" and " + aliasTab + ".idUtilisateur = "+idUtilisateur;
//                }else{
//                    sql =sql +" where " + aliasTab + ".idUtilisateur = "+idUtilisateur;
//                }
//            }
//        }



        return sql;
    }

    private String deriveAlias(String queryString) {
        String query= queryString.replace('\n',' ');
        final Pattern pattern= Pattern.compile(".*FROM\\s+(\\w+)\\s+(\\w+)",Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(query);

        String alias = null;
        if(m.find()){
            alias=m.group(2);
        }
        return alias;
    }

    private String getTableNameFromQuery(String queryString) {
        String query= queryString.replace('\n',' ');
        final Pattern pattern= Pattern.compile(".*FROM\\s+(\\w+)",Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(query);

        String entityName = null;
        if(m.find()){
            entityName=m.group(1);
        }
        return entityName;
    }
}
