package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/5 16:37
 * 修改人：Chen
 * 修改时间：2020/5/5 16:37
 */
public interface CameraView extends BaseView {

    void FindCameraColResult(Object result);
    void AddCameraColResult(Object result);
    void EditCameraColResult(Object result);
    void RemoveCameraColResult(Object result);

}
