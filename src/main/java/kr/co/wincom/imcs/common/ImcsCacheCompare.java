package kr.co.wincom.imcs.common;

import java.io.File;
import java.util.Comparator;

/**
 * 
 * 오래된 순으로 정렬
 * 
 * idollive-cache 폴더 하위에는 앨범ID 로 폴더를 만들 예정임.
 * 오래된 폴더 순으로 정렬해서
 * 캐시 메모리에 저장할 때는 FIFO 정책에 따라서 먼저 입력되고 먼저 제거되게 함.
 * 
 * Comparator 클래스를 ImcsCacheService 클래스에서 익명클래스로 사용하면
 * 컴파일 후에 ..$1.class 같은 파일이 만들어짐
 * 혹시나 운영서버에 소스 반영할 때 실수할 수 있을 것 같아서 ImcsCacheCompare 클래스를 따로 만들었음.
 * 
 * @param <T>
 */
public class ImcsCacheCompare<T> implements Comparator<Object>
{
	@Override
	public int compare(Object object1, Object object2)
	{
		String s1 = "";
		String s2 = "";
		
		s1 = String.valueOf(((File)object1).lastModified());
		s2 = String.valueOf(((File)object2).lastModified());
		
		return s2.compareTo(s1);
	}

}
