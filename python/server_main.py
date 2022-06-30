from flask import Flask, request
import cv2

# 创建Flask实例
app = Flask(__name__)
use_gpu = True


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
    # host默认127.0.0.1 端口默认5000
    app.run(debug=False, port=8082)
