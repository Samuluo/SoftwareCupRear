import paddle
import paddlers as pdrs
import sys
from paddlers.tasks.utils.visualize import visualize_detection
import numpy as np

def objectDetection(path, result_path):
    # 将导出模型所在目录传入Predictor的构造方法中
    predictor = pdrs.deploy.Predictor('model/objectDetection', use_gpu=True)
    # img_file参数指定输入图像路径
    result = predictor.predict(img_file=path)
    visualize_detection(image=path, result=result, threshold=0.5, save_dir=result_path, color=np.asarray([[255, 0, 0]]))
#     print(result[0])
    return result

if __name__=='__main__':
    objectDetection(sys.argv[1], sys.argv[2])
