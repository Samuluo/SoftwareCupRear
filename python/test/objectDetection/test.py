import paddle
import paddlers as pdrs
from skimage.io import imsave
from paddlers.tasks.utils.visualize import visualize_detection
import numpy as np

file_path = "aircraft_233.jpg"
save_path = "./result"
# file_path = "../example/od/uploads/"
# save_path = "../example/od/results"

# 将导出模型所在目录传入Predictor的构造方法中
predictor = pdrs.deploy.Predictor('../../model/objectDetection', use_gpu=True)
print(paddle.device.get_device())
# img_file参数指定输入图像路径

# for i in range(1, 11):
#     result = predictor.predict(img_file=file_path + str(i) + ".jpg")
#     visualize_detection(image=file_path + str(i) + ".jpg", result=result, threshold=0.5, save_dir=save_path, color=np.asarray([[255, 0, 0]]))

# color = np.asarray([[255, 0, 255], [0, 255, 0], [0, 255, 255], [0, 0, 255]], dtype=np.uint8)
result = predictor.predict(img_file=file_path)
visualize_detection(image=file_path, result=result, threshold=0.5, save_dir=save_path)
result = [n for n in result if n['score'] >= 0.5]
print(result)

# print(result)
# imsave(save_path, result, check_contrast=False)
