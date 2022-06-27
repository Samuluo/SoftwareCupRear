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


# file_path = "img.jpg"
# # file_path = "../dw/dataset/img_train/T024109.jpg"
# save_path = "result.png"
file_path = "../example/dw/uploads/"
save_path = "../example/dw/results/"

# 将导出模型所在目录传入Predictor的构造方法中
predictor = pdrs.deploy.Predictor('../../model/terrainClassification', use_gpu=True)
# img_file参数指定输入图像路径
for i in range(1, 11):
    result = predictor.predict(img_file=file_path + str(i) + ".jpg")
    result = result['label_map']
    # print(result)
    result = get_lut()[result]
    cv2.imwrite(save_path + str(i) + ".png", result)

# # 将二值图片黑色部分变透明
# tmp = cv2.cvtColor(result, cv2.COLOR_BGR2GRAY)
# _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
# b, g, r = cv2.split(result)
# rgba = [b, g, r, alpha]
# result = cv2.merge(rgba, 4)
#
# # 注意保存成png格式！！！jpg的话还是黑色背景(255)
# cv2.imwrite(save_path, result)
# cv2.imwrite("result.jpg", result)
