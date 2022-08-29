package com.baoge.wnotes.manager;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.view.TextureView;

import com.baoge.wnotes.dao.DaoMaster;
import com.baoge.wnotes.dao.DaoSession;
import com.baoge.wnotes.dao.DepartmentDao;
import com.baoge.wnotes.dao.DeviceDao;
import com.baoge.wnotes.dao.HospitalDao;
import com.baoge.wnotes.dao.InstallerDao;
import com.baoge.wnotes.dao.OrderDao;
import com.baoge.wnotes.dao.TechnicianDao;
import com.baoge.wnotes.db.City;
import com.baoge.wnotes.db.Department;
import com.baoge.wnotes.db.Device;
import com.baoge.wnotes.db.Hospital;
import com.baoge.wnotes.db.Installer;
import com.baoge.wnotes.db.Order;
import com.baoge.wnotes.db.Technician;
import com.baoge.wnotes.util.DateFormat;
import com.baoge.wnotes.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private static final String DB_NAME = "wnote.db";
    private volatile static DbManager INSTANCE;
    private static Context context;
    private DaoSession daoSession;

    private DbManager() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        daoSession = new DaoMaster(helper.getWritableDb()).newSession();
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    public static DbManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DbManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbManager();
                }
            }
        }
        return INSTANCE;
    }

    public boolean isCityExit(String cityName) {
        boolean isExit = false;
        if (!TextUtils.isEmpty(cityName)) {
            List<City> citys = daoSession.getCityDao().queryBuilder().list();
            if (citys != null && citys.size() > 0) {
                for (City city : citys) {
                    if (TextUtils.equals(cityName, city.getName())) {
                        isExit = true;
                        break;
                    }
                }

            }
        }

        return isExit;

    }

    public boolean insertCity(String cityName) {
        long id = -1;
        if (!TextUtils.isEmpty(cityName)) {

            City city = new City();
            city.setName(cityName);
            id = daoSession.getCityDao().insertOrReplace(city);

        }
        if (id > -1) {
            return true;
        }
        return false;
    }

    public List<String> queryCitysName() {
        List<String> citysName = new ArrayList<>();
        List<City> citys = daoSession.getCityDao().queryBuilder().list();

        if (citys != null && citys.size() > 0) {
            for (City city : citys) {
                citysName.add(city.getName());
            }

        }
        return citysName;

    }


    public void deleteCitys(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            List<City> citys = daoSession.getCityDao().queryBuilder().list();
            if (citys != null && citys.size() > 0) {
                for (City city : citys) {
                    if (TextUtils.equals(cityName, city.getName())) {
                        daoSession.getCityDao().delete(city);
                        break;
                    }
                }

            }
        }

    }

    /***************************************************** HOSPITAL */
    public boolean isHospitalExit(Hospital hospital) {

        if (hospital != null) {
            List<Hospital> hospitals = daoSession.getHospitalDao().queryBuilder().where(HospitalDao.Properties.City.eq(hospital.getCity())).where(HospitalDao.Properties.Name.eq(hospital.getName())).list();
            if (hospitals != null && hospitals.size() > 0) {
                return true;
            }
        }

        return false;

    }

    public boolean insertHospital(Hospital hospital) {
        long id = -1;
        if (hospital != null) {
            id = daoSession.getHospitalDao().insertOrReplace(hospital);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }


    public List<Hospital> queryHospital() {

        return daoSession.getHospitalDao().queryBuilder().list();

    }

    public List<String> queryHospitalNames(String city) {
        List<String> names = new ArrayList<>();
        List<Hospital> hospitals = daoSession.getHospitalDao().queryBuilder().where(HospitalDao.Properties.City.eq(city)).list();
        if (hospitals != null && hospitals.size() > 0) {
            for (Hospital hospital : hospitals) {
                names.add(hospital.getName());
            }
        }
        return names;

    }

    public void deleteHospital(String cityName, String hospitalName) {
        if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(hospitalName)) {
            List<Hospital> hospitals = daoSession.getHospitalDao().queryBuilder().where(HospitalDao.Properties.City.eq(cityName)).where(HospitalDao.Properties.Name.eq(hospitalName)).list();
            if (hospitals != null && hospitals.size() > 0) {
                for (Hospital hospital : hospitals) {
                    daoSession.getHospitalDao().delete(hospital);
                }
            }
        }

    }

    /***************************************************** DEPARTMENG */

    public boolean isDepartmentExit(Department department) {

        if (department != null) {
            List<Department> departments = daoSession.getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.City.eq(department.getCity())).where(DepartmentDao.Properties.Hospital.eq(department.getHospital())).where(DepartmentDao.Properties.Name.eq(department.getName())).list();
            if (departments != null && departments.size() > 0) {
                return true;
            }
        }

        return false;

    }

    public boolean insertDepartment(Department department) {
        long id = -1;
        if (department != null) {
            id = daoSession.getDepartmentDao().insertOrReplace(department);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }


    public List<Department> queryDepartment() {

        return daoSession.getDepartmentDao().queryBuilder().list();

    }

    public List<String> queryDepartmentNames(String city, String hospital) {

        List<String> names = new ArrayList<>();
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(hospital)) {
            return names;
        }
        List<Department> departments = daoSession.getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.City.eq(city)).where(DepartmentDao.Properties.Hospital.eq(hospital)).list();
        if (departments != null && departments.size() > 0) {
            for (Department department : departments) {
                names.add(department.getName());
            }
        }
        return names;

    }

    public void deleteDepartMent(String city, String hospital, String department) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(hospital) || TextUtils.isEmpty(department)) {
            return;
        }


        List<Department> departments = daoSession.getDepartmentDao().queryBuilder().where(DepartmentDao.Properties.City.eq(city)).where(DepartmentDao.Properties.Hospital.eq(hospital)).where(DepartmentDao.Properties.Name.eq(department)).list();
        if (departments != null && departments.size() > 0) {
            for (Department de : departments) {
                daoSession.getDepartmentDao().delete(de);
            }
        }
    }

    /***************************************************** Technician */
    public boolean isTechnicianExit(Technician technician) {

        if (technician != null) {
            List<Technician> technicians = daoSession.getTechnicianDao().queryBuilder().where(TechnicianDao.Properties.City.eq(technician.getCity())).where(TechnicianDao.Properties.Name.eq(technician.getName())).list();
            if (technicians != null && technicians.size() > 0) {
                return true;
            }
        }

        return false;

    }

    public boolean insertTechnician(Technician technician) {
        long id = -1;
        if (technician != null) {
            id = daoSession.getTechnicianDao().insertOrReplace(technician);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }


    public List<String> queryTechnicianNames(String city) {
        List<String> names = new ArrayList<>();
        List<Technician> technicians = daoSession.getTechnicianDao().queryBuilder().where(TechnicianDao.Properties.City.eq(city)).list();
        if (technicians != null && technicians.size() > 0) {
            for (Technician technician : technicians) {
                names.add(technician.getName());
            }
        }
        return names;

    }

    public void deleteTechnician(String city, String name) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(city)) {
            return;
        }
        List<Technician> technicians = daoSession.getTechnicianDao().queryBuilder().where(TechnicianDao.Properties.City.eq(city)).where(TechnicianDao.Properties.Name.eq(name)).list();
        if (technicians != null && technicians.size() > 0) {
            for (Technician technician : technicians) {
                daoSession.getTechnicianDao().delete(technician);
            }
        }
    }


    /***************************************************** Installer */
    public boolean isInstallerExit(Installer installer) {

        if (installer != null) {
            List<Installer> installers = daoSession.getInstallerDao().queryBuilder().where(InstallerDao.Properties.City.eq(installer.getCity())).where(InstallerDao.Properties.Name.eq(installer.getName())).list();
            if (installers != null && installers.size() > 0) {
                return true;
            }
        }

        return false;

    }

    public boolean insertInstaller(Installer installer) {
        long id = -1;
        if (installer != null) {
            id = daoSession.getInstallerDao().insertOrReplace(installer);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }

    public List<String> queryInstallerNames(String city) {
        List<String> names = new ArrayList<>();
        List<Installer> installers = daoSession.getInstallerDao().queryBuilder().where(InstallerDao.Properties.City.eq(city)).list();
        if (installers != null && installers.size() > 0) {
            for (Installer installer : installers) {
                names.add(installer.getName());
            }
        }
        return names;

    }

    public void deleteInstaller(String city, String name) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(city)) {
            return;
        }
        List<Installer> installers = daoSession.getInstallerDao().queryBuilder().where(InstallerDao.Properties.City.eq(city)).where(InstallerDao.Properties.Name.eq(name)).list();
        if (installers != null && installers.size() > 0) {
            for (Installer installer : installers) {
                daoSession.getInstallerDao().delete(installer);
            }
        }
    }


    /***************************************************** Device */
    public boolean isDeviceExit(Device device) {

        if (device != null) {
            List<Device> devices = daoSession.getDeviceDao().queryBuilder().where(DeviceDao.Properties.Name.eq(device.getName())).list();
            if (devices != null && devices.size() > 0) {
                return true;
            }
        }

        return false;

    }

    public boolean insertDevice(Device device) {
        long id = -1;
        if (device != null) {
            id = daoSession.getDeviceDao().insertOrReplace(device);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }

    public List<Device> queryDevices() {

        return daoSession.getDeviceDao().queryBuilder().list();
    }

    public int queryDevicePrice(String deviceName) {

        return daoSession.getDeviceDao().queryBuilder().where(DeviceDao.Properties.Name.eq(deviceName)).list().get(0).getPrice();
    }

    public List<String> queryDeviceNames() {
        List<String> deviceNames = new ArrayList<>();
        List<Device> devices = daoSession.getDeviceDao().queryBuilder().list();
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                deviceNames.add(device.getName());
            }
        }

        return deviceNames;

    }

    public void deleteDevice(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }

        List<Device> devices = daoSession.getDeviceDao().queryBuilder().where(DeviceDao.Properties.Name.eq(name)).list();
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                daoSession.getDeviceDao().delete(device);
            }
        }
    }

    public void deleteDevice(Device device) {
        if (device == null) {
            return;
        }


        daoSession.getDeviceDao().delete(device);

    }

    /***************************************************** Order */


    public boolean insertOrder(Order order) {
        long id = -1;
        if (order != null) {
            id = daoSession.getOrderDao().insertOrReplace(order);
        }
        if (id > -1) {
            return true;
        }
        return false;
    }

    public List<Order> queryOrders() {

        return daoSession.getOrderDao().queryBuilder().list();
    }

    public List<Order> queryOrders(String city) {

        return daoSession.getOrderDao().queryBuilder().where(OrderDao.Properties.City.eq(city)).list();
    }

    public List<Order> queryOrders(String city, long startTime, long endTime) {
        return daoSession.getOrderDao().queryBuilder().where(OrderDao.Properties.City.eq(city)).where(OrderDao.Properties.OrderTime.between(startTime, endTime)).orderAsc(OrderDao.Properties.OrderTime).list();
    }

    public List<Order> queryOrders( long startTime, long endTime) {
        return daoSession.getOrderDao().queryBuilder() .where(OrderDao.Properties.OrderTime.between(startTime, endTime)).orderAsc(OrderDao.Properties.OrderTime).list();
    }
    public void deleteOrder(Order order) {
        if (order != null) {
            daoSession.getOrderDao().delete(order);
        }
    }
}
