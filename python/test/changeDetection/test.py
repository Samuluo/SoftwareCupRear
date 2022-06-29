import time

import paddle
import paddlers as pdrs
from skimage.io import imsave
import cv2

# file1_path = "img1.png"
# file2_path = "cd_img.png"
# save_path = "result.png"
# file1_path = "../example/cd/A/2.png"
# file2_path = "../example/cd/B/2.png"
# save_path = "../example/cd/results/2.png"
file1_path = "../example/cd/A/"
file2_path = "../example/cd/B/"
save_path = "../example/cd/results/"

# 将导出模型所在目录传入Predictor的构造方法中
predictor = pdrs.deploy.Predictor('../../model/changeDetection', use_gpu=True)
print(paddle.device.get_device())
# img_file参数指定输入图像路径
for i in range(1, 11):
    result = predictor.predict(img_file=(file1_path + str(i) + '.png', file2_path + str(i) + '.png'))
    result = result['label_map']
    imsave(save_path + str(i) + '.png', result, check_contrast=False)

# # 将二值图片黑色部分变透明
# im = cv2.imread(save_path)
# tmp = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
# _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
# b, g, r = cv2.split(im)
# rgba = [b, g, r, alpha]
# im = cv2.merge(rgba, 4)
# cv2.imwrite(save_path, im)
