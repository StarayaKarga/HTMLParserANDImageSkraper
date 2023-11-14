import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageScraper {
    public static void main(String[] args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/Oleksandr_Usyk";
        Document doc = Jsoup.connect(url).get();  //Загружаем HTML страницу

        Elements images = doc.select("img");  //Находим все изображения

        String desktopPath = "C:\\Users\\Admin\\Desktop\\Фото с сайта";
        File folder = new File(desktopPath);

        if (!folder.exists()) {  //Проверяем существует ли папка, если нет, то создаем ее методом
            folder.mkdir();
        }

        for (Element image : images) {   //Перебираем все элементы images
            String imageUrl = image.absUrl("src");  //Получаем их URL

            if (imageUrl.startsWith("//")) {  //Если URL начинается с //, то добавляем https:
                imageUrl = "https:" + imageUrl;
            }

            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);  //Выделяем имя файла из URL-адреса и заменяем запрещенный символы на _
            fileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            URL urlObject = new URL(imageUrl); //Создаем URL
            BufferedInputStream in = new BufferedInputStream(urlObject.openStream()); //и с помощью него (URL-а) открывается поток для чтения данных изображения
            FileOutputStream out = new FileOutputStream(desktopPath + "/" + fileName); //Затем создается объект FileOutputStream для записи изображения на диск

            byte[] buffer = new byte[1024];
            int count = 0;

            while ((count = in.read(buffer, 0, 1024)) != -1) { //Далее в цикле читаются блоки данных из потока и записываются в файл на диске
                out.write(buffer, 0, count);
            }

            out.close(); //Закрываем потоки
            in.close();
        }
    }
}
