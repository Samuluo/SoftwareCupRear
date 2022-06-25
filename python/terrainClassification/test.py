import sys
import paddle
import paddlers as pdrs
import numpy as np
import cv2

def get_lut():
    lut = np.zeros((256,3), dtype=np.uint8)
    lut[0] = [255, 0, 0]
    lut[1] = [30, 255, 142]
    lut[2] = [60, 0, 255]
    lut[3] = [255, 222, 0]
    lut[4] = [0, 0, 0]
    return lut

def terrainClassification(path, result_path):
    print(result_path)
    # 将导出模型所在目录传入Predictor的构造方法中
    predictor = pdrs.deploy.Predictor('model/terrainClassification', use_gpu=True)
#     print(paddle.device.get_device())
    # img_file参数指定输入图像路径
    result = predictor.predict(img_file=path)
#     print(result)
    result = result['label_map']

    lut = np.zeros((256,3), dtype=np.uint8)
    lut[0] = [255, 0, 0]
    lut[1] = [30, 255, 142]
    lut[2] = [60, 0, 255]
    lut[3] = [255, 222, 0]
    lut[4] = [0, 0, 0]
    result = lut[result]
    # 将二值图片黑色部分变透明
    tmp = cv2.cvtColor(result, cv2.COLOR_BGR2GRAY)
    _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
    b, g, r = cv2.split(result)
    rgba = [b, g, r, alpha]
    result = cv2.merge(rgba, 4)
    cv2.imwrite(result_path, result)

if __name__=='__main__':
    terrainClassification(sys.argv[1], sys.argv[2])
