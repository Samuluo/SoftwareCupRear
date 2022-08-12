from flask import Flask, request, jsonify
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
# cd_predictor = None
# od_predictor = None
# oe_predictor = None
# tc_predictor = None


# 设置路由，装饰器绑定触发函数
# @app.route("/")
# def hello_world():
#     return "Hello, World!"


# 变化检测
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


# 地物分类
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


# 目标提取
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


# 目标检测
@app.route('/objectDetection', methods=['POST'])
def object_detection():
    # 获取图片地址
    file = request.args["file"]
    result_path = request.args["result_path"]
    # 定义阈值
    threshold = 0.5
    # 开始预测
    result = od_predictor.predict(img_file=file)
    # 保存结果
    visualize_detection(image=file, result=result, threshold=threshold, save_dir=result_path)
    # 返沪预测结果列表
    result = [n for n in result if n['score'] >= threshold]
    return jsonify(result)


# 图片叠加
@app.route('/overlay', methods=['POST'])
def overlay():
    # 获取参数
    img_path = request.args["file"]
    path = request.args["result"]
    result_path = request.args["result_path"]
    need_background = request.args["need_background"]
    if need_background.__eq__("1"):
        need_background = True
    else:
        need_background = False
    target_alpha = float(request.args["a"])
    target_r = int(request.args["r"])
    target_g = int(request.args["g"])
    target_b = int(request.args["b"])
    transparent = request.args["transparent"]
    opacity = request.args["opacity"]
    is_tc = True
    try:
        opacity = float(opacity)
        target_alpha = opacity
    except ValueError:
        is_tc = False

    # 读取图片
    crop_image = cv2.imread(path)  # 结果图
    img = cv2.imread(img_path, cv2.IMREAD_UNCHANGED)  # 原图

    if not is_tc:
        # 将二值图片白色部分变颜色
        Conv_hsv_Gray = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
        res, mask = cv2.threshold(Conv_hsv_Gray, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)
        # 0为需要保留的颜色（即黑色），利用掩膜思想覆盖白色部分
        crop_image[mask == 0] = [target_b, target_g, target_r]

    # 需要背景图
    if need_background or transparent.__eq__("1"):
        # 将二值图片黑色部分变透明
        tmp = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
        _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
        # 修改图片透明度
        alpha = (alpha * target_alpha).astype("uint8")
        b, g, r = cv2.split(crop_image)
        rgba = [b, g, r, alpha]
        result = cv2.merge(rgba, 4)

        # 透明背景
        if transparent.__eq__("1"):
            cv2.imwrite(result_path, result)
            return "叠加成功"

        # 图片叠加
        alpha_result = result[:, :, 3] / 255.0
        alpha_img = 1 - alpha_result
        for c in range(0, 3):
            img[:, :, c] = ((alpha_img * img[:, :, c]) + (alpha_result * result[:, :, c]))

        # 图片保存
        cv2.imwrite(result_path, img)

    # 黑色背景
    else:
        # 修改图片透明度
        b, g, r = cv2.split(crop_image)
        _, _, _, a = cv2.split(cv2.imread(path, cv2.IMREAD_UNCHANGED))
        a = (a * target_alpha).astype("uint8")
        a[a == 0] = 255
        result = cv2.merge((b, g, r, a))
        # 图片保存
        cv2.imwrite(result_path, result)
    return "叠加成功"


if __name__ == "__main__":
    # debug=True 代码修改能运行时生效，app.run运行服务
    # host默认127.0.0.1 端口号为8082
    app.run(debug=False, port=8082)
