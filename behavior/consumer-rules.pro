
# com.angcyo.widget.recycler.RecyclerExKt.getLastVelocity 获取RV的Fling的速率
-keepclassmembers class androidx.recyclerview.widget.RecyclerView {
   androidx.recyclerview.widget.RecyclerView$ViewFlinger mViewFlinger;
   #<fields>;
}
-keepclassmembers class androidx.recyclerview.widget.RecyclerView$ViewFlinger {
   android.widget.OverScroller mOverScroller;
   #<fields>;
}
-keepclassmembers class androidx.core.widget.NestedScrollView {
   android.widget.OverScroller mScroller;
   #<fields>;
}
# end...