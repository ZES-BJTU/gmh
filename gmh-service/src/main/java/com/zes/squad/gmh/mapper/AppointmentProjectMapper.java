package com.zes.squad.gmh.mapper;

import java.util.List;
import java.util.Map;

import com.zes.squad.gmh.entity.po.AppointmentProjectPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectUnion;
import com.zes.squad.gmh.entity.union.OperatorTimeTableUnion;

public interface AppointmentProjectMapper {

    int insert(AppointmentProjectPo appointmentProjectPo);

    List<AppointmentProjectUnion> getAppointmentProjectUnionByAppId(Long appoentmentId);
    
    int delById(Long id);

    int delByAppointmentId(Long appointmentId);
    
    List<AppointmentProjectPo> getListByAppointmentId(Long appointmentId);

	List<OperatorTimeTableUnion> queryOperatorTimeTable(Map<String, Object> map);
}
