package com.ourbuaa.buaahelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
//import com.yanzhenjie.swiperecyclerview.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NoticeListFragment extends Fragment implements BUAA_RecyclerViewAdapter.OnClickListener,BUAA_RecyclerViewAdapter.GarbageCollector {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
   private BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter;
 //   private RecyclerView.OnScrollListener onScrollListener;
 //   private RecyclerView recyclerView;
    private ContentProvider provider, trash;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private ItemTouchHelper itemTouchHelper;
    private BUAAItemTouchHelperCallback buaaItemTouchHelperCallback;
    private SQLiteUtils sqLiteUtils;
    private View view;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
   // private MenuAdapter mMenuAdapter;
    private List<String> mDataList;
    private int size = 50;

    public void setSqLiteUtils(SQLiteUtils sqLiteUtils) {
        this.sqLiteUtils = sqLiteUtils;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticeListFragment newInstance(int columnCount) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        mContext = view.getContext();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView)view.findViewById(R.id.recycler_view);

        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));// 添加分割线。
        // 添加滚动监听。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);
        ((ListScrollListener)mOnScrollListener).setBuaaContentProvider((BUAAContentProvider)provider);
        // 为SwipeRecyclerView的Item创建菜单
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

       /* mDataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mDataList.add("我是第" + i + "个。");
        }*/


        //mMenuAdapter = new MenuAdapter(mDataList);
        //mMenuAdapter.setOnItemClickListener(onItemClickListener);
        ((BUAAContentProvider)provider).setContext(getActivity());

        if (buaa_recyclerViewAdapter == null)
            buaa_recyclerViewAdapter = new BUAA_RecyclerViewAdapter(provider);
        buaa_recyclerViewAdapter.setOnClickListener(this);
        ((BUAAContentProvider) provider).setBuaa_recyclerViewAdapter(buaa_recyclerViewAdapter);
        //buaaItemTouchHelperCallback = new BUAAItemTouchHelperCallback(buaa_recyclerViewAdapter);
        buaa_recyclerViewAdapter.setGarbageCollector(this);
        mSwipeMenuRecyclerView.setAdapter(buaa_recyclerViewAdapter);
        //itemTouchHelper = new ItemTouchHelper(buaaItemTouchHelperCallback);
        //itemTouchHelper.attachToRecyclerView(mSwipeMenuRecyclerView);
       /* recyclerView = (RecyclerView) view.findViewById(R.id.NoticeList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //provider = new ContentProvider();
        ((BUAAContentProvider)provider).setContext(getActivity());

        if (buaa_recyclerViewAdapter == null)
            buaa_recyclerViewAdapter = new BUAA_RecyclerViewAdapter(provider);
        buaa_recyclerViewAdapter.setOnClickListener(this);
        ((BUAAContentProvider) provider).setBuaa_recyclerViewAdapter(buaa_recyclerViewAdapter);
        buaaItemTouchHelperCallback = new BUAAItemTouchHelperCallback(buaa_recyclerViewAdapter);
        buaa_recyclerViewAdapter.setGarbageCollector(this);
        // buaaItemTouchHelperCallback.setMadapter(buaa_recyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(buaaItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(buaa_recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (onScrollListener == null)
            recyclerView.addOnScrollListener(new DefaultOnScrollListener());
        else
            recyclerView.addOnScrollListener(onScrollListener);
        /*DividerDecoration divider = new DividerDecoration.Builder(getContext(),mLrecyclerviewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
        mRecyclerView.addItemDecoration(divider);*/

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            //if (buaa_recyclerViewAdapter != null)
            //    buaa_recyclerViewAdapter.setOnListFragmentInteractionListener(mListener);
        } else {
           // throw new RuntimeException(context.toString()
             //       + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BUAAContentProvider) provider).updateNotifications();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(CommonItemForList item);
    }

    @Override
    public void OnItemClick(CommonItemForList itemForList, int pos) {
        // BUAA_RecyclerViewAdapter.ListItemViewHolder viewHolder = (BUAA_RecyclerViewAdapter.ListItemViewHolder)(recyclerView.getChildViewHolder(view));
        // CommonItemForList itemForList = viewHolder.getItemForList();
        //Bundle bundle = new Bundle();
        // bundle.putInt("ID",itemForList.getId() );
        //bundle.putInt();
        sqLiteUtils.ReadNotificationByID(itemForList.getId());
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("ID", itemForList.getId());
        intent.putExtra("Position", pos);
        // if (itemForList.Detail != null)
        //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
        //Toast.makeText(getActivity(),"Position:"+pos,Toast.LENGTH_SHORT).show();
    }

  @Override
    public void OnDeleteBtnClick(int pos) {
        //int pos = recyclerView.getChildAdapterPosition(view);
       // buaa_recyclerViewAdapter.RemoveData(pos);
       // buaa_recyclerViewAdapter.notifyDataSetChanged();
    }

   @Override
    public void OnFavoriteBtnClick(int pos) {

    }

   @Override
    public void OnItemClick(int pos) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        //intent.putExtra("ID",itemForList.getId() );
        intent.putExtra("Position", pos);
        // if (itemForList.Detail != null)
        //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
    }

    @Override
    public void OnDataRemoved(CommonItemForList itemForList, int position) {
        //TODO:删除item的接口已经暴露,position表示待删除元素在ITEMS数组中的index，itemForList是待删除元素的详细内容
        //if (trash instanceof BUAAContentProvider) {
        //得到type为TRASH的ContentProvider
        //BUAAContentProvider garbageCollector = (BUAAContentProvider)trash;
        //这是目前默认的删除方式，记得保留这几行
        String type = ((BUAAContentProvider) provider).getType();
        if (!type.equals(BUAAContentProvider.Trash)) {
            sqLiteUtils.HideNotificationByID(itemForList.getId());
        } else
            sqLiteUtils.SeekNotificationByID(itemForList.getId());
        if (provider instanceof BUAAContentProvider) {
            ((BUAAContentProvider) provider).deleteDataInList(position);
        }

        //}
    }

  public void setProvider(ContentProvider provider) {
        this.provider = provider;
    }

   public void setTrash(ContentProvider trash) {
        this.trash = trash;
    }

    public void setAdapter(BUAA_RecyclerViewAdapter adapter) {
        buaa_recyclerViewAdapter = adapter;
    }
    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeMenuRecyclerView.postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 2000);
        }
    };

    /**
     * 加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new ListScrollListener();
  /*  private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。

                Toast.makeText(mContext, "滑到最底部了，去加载更多吧！", Toast.LENGTH_SHORT).show();
                size += 50;
                for (int i = size - 50; i < size; i++) {
                    mDataList.add("我是第" + i + "个。");
                }
                mMenuAdapter.notifyDataSetChanged();
            }
        }
    };*/

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                        .setImage(R.mipmap.ic_action_add) // 图标。
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);

                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
            }
        }
    };

   /* private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };*/

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                buaa_recyclerViewAdapter.RemoveData(adapterPosition);
                buaa_recyclerViewAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_all_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
            mSwipeMenuRecyclerView.smoothOpenRightMenu(0);
        }
        return true;
    }*/
}