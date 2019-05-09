package cn.mmvtc.mobliesafe.chapter02.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cn.mmvtc.mobliesafe.chapter02.entity.ContactInfo;

/**
 * Created by Administrator on 2019/4/13.
 * 功能：解析联系人
 * 先获取联系人的ID 根据id在data表中查询联系人名字及
 * 电话号码，封装到ContactInfo中存入到list集合
 * 通过：内容访问者ContentResolver
 * 操作步骤：
 * 1.获取内容访问者
 * 2.用uri查询raw——contacts表中的id，存放到游标，遍历游标。
 * 3.根据id得到name，phone
 */

public class ContactInfoParser {
    public static List<ContactInfo> getSystemContact(Context context) {
        ContentResolver resolver = context.getContentResolver();
        //1.查询raw_contacts表，吧联系人的id取出来
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");//第一个表保存到uri对象中
        Uri datauri = Uri.parse("content://com.android.contacts/data");//第一个表保存到uri对象中
        List<ContactInfo> infos = new ArrayList<ContactInfo>();//存放联系人信息的集合
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}
                , null, null, null);//集合的类型是游标（也可是当做数组），需要遍历
        //遍历第一步
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);//获取cursor的第一个id值
            if (id != null) {
                //id不为空
                ContactInfo info = new ContactInfo();
                info.id = id;//保存id值到实体类中
                //2.根据联系人的id，查询data表，把这个id的数据取出来
                //系统API查询data表的时候，不是真正的查询data表，而是查询data表的视图
                //第二步根据id查询data表，提取名字和电话号码
                Cursor dataCursor = resolver.query(datauri, new String[]{
                        "data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                //raw_contact_id=contact_id字符串最后排序为空
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    //判断是否是联系人名字
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        info.name = data1;
                        //判断是否是联系人的联系的电话号码
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        info.phone = data1;
                    }
                }
                infos.add(info);
                dataCursor.close();

            }
        }
        cursor.close();
        return infos;
    }

    public static List<ContactInfo> getSimContacts(Context context) {
        Uri uri = Uri.parse("content://icc/adn");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
        if(mCursor !=null){
        while (mCursor.moveToNext()) {
            ContactInfo info = new ContactInfo();
            //取得联系人名字
            int nameFieldColumnIndex = mCursor.getColumnIndex("name");
            info.name = mCursor.getString(nameFieldColumnIndex);
            //获取电话号码
            int numberFileldColumnIndex = mCursor.getColumnIndex("number");
            info.phone = mCursor.getString(numberFileldColumnIndex);
            infos.add(info);
        }
    }

    mCursor.close();
    return infos;
    }
}
