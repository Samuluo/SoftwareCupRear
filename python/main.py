from flask import Flask, request
import paddlers as pdrs
from skimage.io import imsave
import cv2
import numpy as np
from paddlers.tasks.utils.visualize import visualize_detection

# 创建Flask实例
app = Flask(__name__)
use_gpu = True
# 初始化predictor
cd_predictor = pdrs.deploy.Predictor('model/changeDetection', use_gpu=use_gpu)
od_predictor = pdrs.deploy.Predictor('model/objectDetection', use_gpu=use_gpu)
oe_predictor = pdrs.deploy.Predictor('model/objectExtraction', use_gpu=use_gpu)
tc_predictor = pdrs.deploy.Predictor('model/terrainClassification', use_gpu=use_gpu)


# 设置路由，装饰器绑定触发函数
@app.route("/")
def hello_world():
    return "Hello, World!"


# cd
@app.route('/changeDetection', methods=['POST'])
def change_detection():
    # 获取图片地址
    file1 = request.args["file1"]
    file2 = request.args["file2"]
    result_path = request.args["result_path"]
    # 开始预测
    result = cd_predictor.predict(img_file=(file1, file2))
    result = result['label_map']
    # 保存结果（暂存）
    imsave(result_path + ".png", result, check_contrast=False)
    # 将二值图片黑色部分变透明
    im = cv2.imread(result_path + '.png')
    tmp = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
    _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
    b, g, r = cv2.split(im)
    rgba = [b, g, r, alpha]
    im = cv2.merge(rgba, 4)
    # 分别保存透明结果与黑色结果
    cv2.imwrite(result_path + '.png', im)
    cv2.imwrite(result_path + '.jpg', im)
    return "预测成功"


# dw
@app.route('/terrainClassification', methods=['POST'])
def terrain_classification():
    # 获取图片地址
    file = request.args["file"]
    result_path = request.args["result_path"]
    # 开始预测
    result = tc_predictor.predict(img_file=file)
    result = result['label_map']
    # 绘制结果图片
    lut = np.zeros((256, 3), dtype=np.uint8)
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
    # 分别保存透明结果与黑色结果
    cv2.imwrite(result_path + '.png', result)
    cv2.imwrite(result_path + '.jpg', result)
    return "预测成功"


# oe
@app.route('/objectExtraction', methods=['POST'])
def target_extraction():
    # 获取图片地址
    file = request.args["file"]
    result_path = request.args["result_path"]
    # 开始预测
    result = oe_predictor.predict(img_file=file)
    result = result['label_map']
    # 保存结果（暂存）
    imsave(result_path + '.png', result, check_contrast=False)
    # 将二值图片黑色部分变透明
    im = cv2.imread(result_path + '.png')
    tmp = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
    _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
    b, g, r = cv2.split(im)
    rgba = [b, g, r, alpha]
    im = cv2.merge(rgba, 4)
    # 分别保存透明结果与黑色结果
    cv2.imwrite(result_path + '.png', im)
    cv2.imwrite(result_path + '.jpg', im)
    return "预测成功"


# od
@app.route('/objectDetection', methods=['POST'])
def object_detection():
    # 获取图片地址
    file = request.args["file"]
    result_path = request.args["result_path"]
    # 开始预测
    result = od_predictor.predict(img_file=file)
    # 保存结果
    visualize_detection(image=file, result=result, threshold=0.5, save_dir=result_path, color=np.asarray([[255, 0, 0]]))
    return "预测成功"


if __name__ == "__main__":
    # debug=True 代码修改能运行时生效，app.run运行服务
    # host默认127.0.0.1 端口默认5000
    app.run(debug=False, port=8081)
